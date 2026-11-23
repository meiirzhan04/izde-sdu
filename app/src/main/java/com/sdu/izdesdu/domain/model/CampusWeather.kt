package com.sdu.izdesdu.domain.model

data class CampusWeather(
    val temperatureC: Int,
    val feelsLikeC: Int,
    val windSpeedKmh: Int,
    val humidityPercent: Int,
    val condition: WeatherCondition,
    val iconUrl: String,
    val isDay: Boolean,
    val updatedTime: String,
    val hourly: List<HourlyWeather>
)

data class HourlyWeather(
    val timeLabel: String,
    val temperatureC: Int,
    val condition: WeatherCondition,
    val iconUrl: String
)
