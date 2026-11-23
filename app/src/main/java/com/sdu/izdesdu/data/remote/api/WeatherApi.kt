package com.sdu.izdesdu.data.remote.api

import com.sdu.izdesdu.data.remote.dto.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/forecast")
    suspend fun getCampusWeather(
        @Query("latitude") latitude: Double = CAMPUS_LATITUDE,
        @Query("longitude") longitude: Double = CAMPUS_LONGITUDE,
        @Query("current") current: String = CURRENT_FIELDS,
        @Query("hourly") hourly: String = HOURLY_FIELDS,
        @Query("timezone") timezone: String = CAMPUS_TIMEZONE,
        @Query("forecast_days") forecastDays: Int = FORECAST_DAYS
    ): WeatherResponseDto

    companion object {
        const val BASE_URL = "https://api.open-meteo.com/"
        const val CAMPUS_LATITUDE = 43.2077
        const val CAMPUS_LONGITUDE = 76.6697
        const val CAMPUS_TIMEZONE = "Asia/Almaty"
        const val FORECAST_DAYS = 1
        const val CURRENT_FIELDS =
            "temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m,is_day"
        const val HOURLY_FIELDS = "temperature_2m,weather_code,is_day"
    }
}
