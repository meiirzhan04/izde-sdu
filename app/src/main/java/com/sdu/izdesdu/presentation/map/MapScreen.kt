package com.sdu.izdesdu.presentation.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sdu.izdesdu.presentation.weather.WeatherViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MapScreen(
    onStartRoute: (String) -> Unit,
    onOpenPlace: (String) -> Unit,
    onOpenWeather: () -> Unit,
    weatherViewModel: WeatherViewModel = koinViewModel()
) {
    val viewModel = koinViewModel<MapViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val weatherState by weatherViewModel.uiState.collectAsStateWithLifecycle()

    MapScreenContent(
        uiState = uiState,
        weatherState = weatherState,
        onSelectPlace = viewModel::selectPlace,
        onStartRoute = onStartRoute,
        onOpenPlace = onOpenPlace,
        onOpenWeather = onOpenWeather,
        onRetryWeather = weatherViewModel::retry
    )
}
