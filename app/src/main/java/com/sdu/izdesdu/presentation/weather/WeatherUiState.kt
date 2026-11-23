package com.sdu.izdesdu.presentation.weather

import com.sdu.izdesdu.domain.model.CampusWeather
import com.sdu.izdesdu.domain.model.WeatherErrorType

sealed interface WeatherUiState {
    data object Loading : WeatherUiState
    data class Success(val weather: CampusWeather) : WeatherUiState
    data class Error(val errorType: WeatherErrorType = WeatherErrorType.UNKNOWN) : WeatherUiState
    data object Empty : WeatherUiState
}
