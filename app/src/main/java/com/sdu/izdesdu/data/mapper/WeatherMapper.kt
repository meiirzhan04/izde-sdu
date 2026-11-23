package com.sdu.izdesdu.data.mapper

import com.sdu.izdesdu.data.remote.dto.HourlyWeatherDto
import com.sdu.izdesdu.data.remote.dto.WeatherResponseDto
import com.sdu.izdesdu.domain.model.CampusWeather
import com.sdu.izdesdu.domain.model.HourlyWeather
import com.sdu.izdesdu.domain.model.WeatherCondition
import kotlin.math.roundToInt

object WeatherMapper {
    private const val ICON_URL_PREFIX = "https://openweathermap.org/img/wn/"
    private const val ICON_URL_SUFFIX = "@2x.png"

    fun toDomain(dto: WeatherResponseDto): CampusWeather? {
        val current = dto.current ?: return null
        val temperature = current.temperature2m ?: return null
        val weatherCode = current.weatherCode ?: 0
        val isDay = current.isDay != 0
        val updatedTime = extractHourMinute(current.time)
        val hourlyItems = mapHourly(dto.hourly, updatedTime)

        return CampusWeather(
            temperatureC = temperature.roundToInt(),
            feelsLikeC = (current.apparentTemperature ?: temperature).roundToInt(),
            windSpeedKmh = (current.windSpeed10m ?: 0.0).roundToInt(),
            humidityPercent = current.relativeHumidity2m ?: 0,
            condition = mapCondition(weatherCode),
            iconUrl = buildIconUrl(weatherCode, isDay),
            isDay = isDay,
            updatedTime = updatedTime,
            hourly = hourlyItems
        )
    }

    private fun mapHourly(
        hourly: HourlyWeatherDto?,
        currentHourMinute: String
    ): List<HourlyWeather> {
        if (hourly == null) return emptyList()
        val count = minOf(
            hourly.time.size,
            hourly.temperature2m.size,
            hourly.weatherCode.size
        )
        if (count == 0) return emptyList()

        val allItems = (0 until count).map { index ->
            val code = hourly.weatherCode[index]
            val isDay = hourly.isDay.getOrNull(index) != 0
            HourlyWeather(
                timeLabel = extractHourMinute(hourly.time[index]),
                temperatureC = hourly.temperature2m[index].roundToInt(),
                condition = mapCondition(code),
                iconUrl = buildIconUrl(code, isDay)
            )
        }

        val currentHourPrefix = currentHourMinute.take(2)
        val startIndex = allItems.indexOfFirst { it.timeLabel.startsWith(currentHourPrefix) }
        return if (startIndex in allItems.indices) {
            allItems.subList(startIndex, allItems.size)
        } else {
            allItems
        }
    }

    private fun extractHourMinute(rawTime: String?): String {
        if (rawTime.isNullOrBlank()) return ""
        return rawTime.substringAfter('T', rawTime).take(5)
    }

    fun mapCondition(weatherCode: Int): WeatherCondition = when (weatherCode) {
        0 -> WeatherCondition.CLEAR
        1, 2 -> WeatherCondition.PARTLY_CLOUDY
        3 -> WeatherCondition.CLOUDY
        45, 48 -> WeatherCondition.FOG
        51, 53, 55, 56, 57 -> WeatherCondition.DRIZZLE
        61, 63, 65, 66, 67 -> WeatherCondition.RAIN
        71, 73, 75, 77, 85, 86 -> WeatherCondition.SNOW
        80, 81, 82 -> WeatherCondition.SHOWERS
        95, 96, 99 -> WeatherCondition.THUNDERSTORM
        else -> WeatherCondition.CLOUDY
    }

    fun buildIconUrl(weatherCode: Int, isDay: Boolean): String {
        val period = if (isDay) "d" else "n"
        val iconCode = when (weatherCode) {
            0 -> "01$period"
            1, 2 -> "02$period"
            3 -> "04$period"
            45, 48 -> "50$period"
            51, 53, 55, 56, 57 -> "09$period"
            61, 63, 65, 66, 67 -> "10$period"
            71, 73, 75, 77, 85, 86 -> "13$period"
            80, 81, 82 -> "09$period"
            95, 96, 99 -> "11$period"
            else -> "03$period"
        }
        return "$ICON_URL_PREFIX$iconCode$ICON_URL_SUFFIX"
    }
}
