package com.sdu.izdesdu.data.mapper

import com.sdu.izdesdu.data.remote.dto.CurrentWeatherDto
import com.sdu.izdesdu.data.remote.dto.HourlyWeatherDto
import com.sdu.izdesdu.data.remote.dto.WeatherResponseDto
import com.sdu.izdesdu.domain.model.WeatherCondition
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherMapperTest {

    @Test
    fun toDomain_mapsCurrentAndFiltersHourlyFromCurrentHour() {
        val dto = WeatherResponseDto(
            current = CurrentWeatherDto(
                time = "2026-11-23T13:15",
                temperature2m = 13.6,
                relativeHumidity2m = 52,
                apparentTemperature = 11.4,
                weatherCode = 2,
                windSpeed10m = 8.7,
                isDay = 1
            ),
            hourly = HourlyWeatherDto(
                time = listOf(
                    "2026-11-23T12:00",
                    "2026-11-23T13:00",
                    "2026-11-23T14:00"
                ),
                temperature2m = listOf(12.2, 13.6, 15.1),
                weatherCode = listOf(3, 2, 0),
                isDay = listOf(1, 1, 1)
            )
        )

        val weather = WeatherMapper.toDomain(dto)

        assertNotNull(weather)
        assertEquals(14, weather!!.temperatureC)
        assertEquals(11, weather.feelsLikeC)
        assertEquals(9, weather.windSpeedKmh)
        assertEquals(52, weather.humidityPercent)
        assertEquals(WeatherCondition.PARTLY_CLOUDY, weather.condition)
        assertEquals("https://openweathermap.org/img/wn/02d@2x.png", weather.iconUrl)
        assertEquals("13:15", weather.updatedTime)
        assertEquals(2, weather.hourly.size)
        assertEquals("13:00", weather.hourly[0].timeLabel)
        assertEquals("14:00", weather.hourly[1].timeLabel)
        assertEquals("https://openweathermap.org/img/wn/01d@2x.png", weather.hourly[1].iconUrl)
    }

    @Test
    fun toDomain_returnsNullWhenCurrentOrTemperatureMissing() {
        assertNull(WeatherMapper.toDomain(WeatherResponseDto()))
        assertNull(
            WeatherMapper.toDomain(
                WeatherResponseDto(current = CurrentWeatherDto(temperature2m = null))
            )
        )
    }

    @Test
    fun toDomain_returnsEmptyHourlyWhenHourlyMissing() {
        val weather = WeatherMapper.toDomain(
            WeatherResponseDto(
                current = CurrentWeatherDto(
                    time = "2026-11-23T21:00",
                    temperature2m = 4.0,
                    weatherCode = 61,
                    isDay = 0
                )
            )
        )

        assertNotNull(weather)
        assertTrue(weather!!.hourly.isEmpty())
        assertEquals(WeatherCondition.RAIN, weather.condition)
        assertEquals("https://openweathermap.org/img/wn/10n@2x.png", weather.iconUrl)
    }

    @Test
    fun mapCondition_coversMainWmoWeatherCodes() {
        assertEquals(WeatherCondition.CLEAR, WeatherMapper.mapCondition(0))
        assertEquals(WeatherCondition.CLOUDY, WeatherMapper.mapCondition(3))
        assertEquals(WeatherCondition.FOG, WeatherMapper.mapCondition(45))
        assertEquals(WeatherCondition.DRIZZLE, WeatherMapper.mapCondition(53))
        assertEquals(WeatherCondition.RAIN, WeatherMapper.mapCondition(63))
        assertEquals(WeatherCondition.SNOW, WeatherMapper.mapCondition(73))
        assertEquals(WeatherCondition.SHOWERS, WeatherMapper.mapCondition(81))
        assertEquals(WeatherCondition.THUNDERSTORM, WeatherMapper.mapCondition(95))
    }

    @Test
    fun toDomain_skipsNullableAndBlankHourlyEntries() {
        val dto = WeatherResponseDto(
            current = CurrentWeatherDto(
                time = "2026-11-23T13:10",
                temperature2m = 10.0,
                weatherCode = 0,
                isDay = 1
            ),
            hourly = HourlyWeatherDto(
                time = listOf(null, "2026-11-23T13:00", "", "2026-11-23T14:00"),
                temperature2m = listOf(9.0, null, 11.0, 12.4),
                weatherCode = listOf(0, 1, 2, null),
                isDay = listOf(1, 1, 1, null)
            )
        )

        val weather = WeatherMapper.toDomain(dto)

        assertNotNull(weather)
        assertEquals(1, weather!!.hourly.size)
        assertEquals("14:00", weather.hourly[0].timeLabel)
        assertEquals(12, weather.hourly[0].temperatureC)
        assertEquals(WeatherCondition.CLEAR, weather.hourly[0].condition)
        assertEquals("https://openweathermap.org/img/wn/01d@2x.png", weather.hourly[0].iconUrl)
    }
}
