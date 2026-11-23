package com.sdu.izdesdu.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponseDto(
    @SerialName("current")
    val current: CurrentWeatherDto? = null,
    @SerialName("hourly")
    val hourly: HourlyWeatherDto? = null
)

@Serializable
data class CurrentWeatherDto(
    @SerialName("time")
    val time: String? = null,
    @SerialName("temperature_2m")
    val temperature2m: Double? = null,
    @SerialName("relative_humidity_2m")
    val relativeHumidity2m: Int? = null,
    @SerialName("apparent_temperature")
    val apparentTemperature: Double? = null,
    @SerialName("weather_code")
    val weatherCode: Int? = null,
    @SerialName("wind_speed_10m")
    val windSpeed10m: Double? = null,
    @SerialName("is_day")
    val isDay: Int? = null
)

@Serializable
data class HourlyWeatherDto(
    @SerialName("time")
    val time: List<String?> = emptyList(),
    @SerialName("temperature_2m")
    val temperature2m: List<Double?> = emptyList(),
    @SerialName("weather_code")
    val weatherCode: List<Int?> = emptyList(),
    @SerialName("is_day")
    val isDay: List<Int?> = emptyList()
)
