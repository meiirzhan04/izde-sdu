package com.sdu.izdesdu.presentation.weather

import com.sdu.izdesdu.domain.model.CampusWeather

sealed interface WeatherUiState {
    data object Loading : WeatherUiState
    data class Success(val weather: CampusWeather) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
    data object Empty : WeatherUiState
}
