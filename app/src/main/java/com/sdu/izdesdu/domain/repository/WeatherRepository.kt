package com.sdu.izdesdu.domain.repository

import com.sdu.izdesdu.domain.model.CampusWeather

interface WeatherRepository {
    suspend fun getCampusWeather(): Result<CampusWeather?>
}
