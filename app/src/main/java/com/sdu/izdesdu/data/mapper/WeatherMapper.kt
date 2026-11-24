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
    private const val ISO_TIME_DELIMITER = 'T'
    private const val TIME_LABEL_LENGTH = 5
    private const val HOUR_PREFIX_LENGTH = 2

    private data class WmoMapping(
        val condition: WeatherCondition,
        val iconPrefix: String
    )

    fun toDomain(dto: WeatherResponseDto): CampusWeather? {
        val current = dto.current ?: return null
        val temperature = current.temperature2m ?: return null
        val weatherCode = current.weatherCode ?: 0
        val isDay = current.isDay != 0
        val updatedTime = extractHourMinute(current.time)
        val wmo = resolveWmo(weatherCode)

        return CampusWeather(
            temperatureC = temperature.roundToInt(),
            feelsLikeC = (current.apparentTemperature ?: temperature).roundToInt(),
            windSpeedKmh = (current.windSpeed10m ?: 0.0).roundToInt(),
            humidityPercent = current.relativeHumidity2m ?: 0,
            condition = wmo.condition,
            iconUrl = formatIconUrl(wmo.iconPrefix, isDay),
            isDay = isDay,
            updatedTime = updatedTime,
            hourly = mapHourly(dto.hourly, updatedTime)
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

        val allItems = (0 until count).mapNotNull { index ->
            val timeLabel = extractHourMinute(hourly.time[index])
            val temperature = hourly.temperature2m[index]
            if (timeLabel.isBlank() || temperature == null) {
                return@mapNotNull null
            }
            val code = hourly.weatherCode[index] ?: 0
            val isDay = hourly.isDay.getOrNull(index) != 0
            val wmo = resolveWmo(code)
            HourlyWeather(
                timeLabel = timeLabel,
                temperatureC = temperature.roundToInt(),
                condition = wmo.condition,
                iconUrl = formatIconUrl(wmo.iconPrefix, isDay)
            )
        }
        if (allItems.isEmpty()) return emptyList()

        val currentHourPrefix = currentHourMinute.take(HOUR_PREFIX_LENGTH)
        if (currentHourPrefix.length < HOUR_PREFIX_LENGTH) return allItems
        val startIndex = allItems.indexOfFirst {
            it.timeLabel.take(HOUR_PREFIX_LENGTH) >= currentHourPrefix
        }
        return if (startIndex >= 0) allItems.drop(startIndex) else allItems
    }

    private fun extractHourMinute(rawTime: String?): String {
        if (rawTime.isNullOrBlank()) return ""
        return rawTime.substringAfter(ISO_TIME_DELIMITER, rawTime).take(TIME_LABEL_LENGTH)
    }

    fun mapCondition(weatherCode: Int): WeatherCondition = resolveWmo(weatherCode).condition

    fun buildIconUrl(weatherCode: Int, isDay: Boolean): String =
        formatIconUrl(resolveWmo(weatherCode).iconPrefix, isDay)

    private fun formatIconUrl(iconPrefix: String, isDay: Boolean): String {
        val period = if (isDay) "d" else "n"
        return "$ICON_URL_PREFIX$iconPrefix$period$ICON_URL_SUFFIX"
    }

    private fun resolveWmo(weatherCode: Int): WmoMapping = when (weatherCode) {
        0 -> WmoMapping(WeatherCondition.CLEAR, "01")
        1, 2 -> WmoMapping(WeatherCondition.PARTLY_CLOUDY, "02")
        3 -> WmoMapping(WeatherCondition.CLOUDY, "04")
        45, 48 -> WmoMapping(WeatherCondition.FOG, "50")
        51, 53, 55, 56, 57 -> WmoMapping(WeatherCondition.DRIZZLE, "09")
        61, 63, 65, 66, 67 -> WmoMapping(WeatherCondition.RAIN, "10")
        71, 73, 75, 77, 85, 86 -> WmoMapping(WeatherCondition.SNOW, "13")
        80, 81, 82 -> WmoMapping(WeatherCondition.SHOWERS, "09")
        95, 96, 99 -> WmoMapping(WeatherCondition.THUNDERSTORM, "11")
        else -> WmoMapping(WeatherCondition.CLOUDY, "03")
    }
}
