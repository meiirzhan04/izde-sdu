package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.model.CampusWeather
import com.sdu.izdesdu.domain.repository.WeatherRepository

class GetCampusWeatherUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(): Result<CampusWeather?> = weatherRepository.getCampusWeather()
}
