package com.sdu.izdesdu.presentation.weather

import androidx.annotation.StringRes
import com.sdu.izdesdu.R
import com.sdu.izdesdu.domain.model.WeatherCondition
import com.sdu.izdesdu.domain.model.WeatherErrorType

@StringRes
fun WeatherCondition.labelRes(): Int = when (this) {
    WeatherCondition.CLEAR -> R.string.weather_cond_clear
    WeatherCondition.PARTLY_CLOUDY -> R.string.weather_cond_partly_cloudy
    WeatherCondition.CLOUDY -> R.string.weather_cond_cloudy
    WeatherCondition.FOG -> R.string.weather_cond_fog
    WeatherCondition.DRIZZLE -> R.string.weather_cond_drizzle
    WeatherCondition.RAIN -> R.string.weather_cond_rain
    WeatherCondition.SNOW -> R.string.weather_cond_snow
    WeatherCondition.SHOWERS -> R.string.weather_cond_showers
    WeatherCondition.THUNDERSTORM -> R.string.weather_cond_thunderstorm
}

@StringRes
fun WeatherCondition.campusAdviceRes(temperatureC: Int): Int = when {
    this == WeatherCondition.THUNDERSTORM -> R.string.weather_advice_thunderstorm
    this == WeatherCondition.RAIN || this == WeatherCondition.SHOWERS || this == WeatherCondition.DRIZZLE ->
        R.string.weather_advice_rain
    this == WeatherCondition.SNOW -> R.string.weather_advice_snow
    this == WeatherCondition.FOG -> R.string.weather_advice_fog
    temperatureC <= 5 -> R.string.weather_advice_cold
    this == WeatherCondition.CLEAR -> R.string.weather_advice_clear
    else -> R.string.weather_advice_cloudy
}

@StringRes
fun WeatherErrorType.messageRes(): Int = when (this) {
    WeatherErrorType.NETWORK -> R.string.weather_error_network
    WeatherErrorType.TIMEOUT -> R.string.weather_error_timeout
    WeatherErrorType.SERVER -> R.string.weather_error_server
    WeatherErrorType.UNKNOWN -> R.string.weather_error_default
}
