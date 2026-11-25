package com.sdu.izdesdu.presentation.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sdu.izdesdu.presentation.weather.WeatherViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MapScreen(
    onStartRoute: (String) -> Unit,
    onOpenPlace: (String) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenWeather: () -> Unit,
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel = koinViewModel(),
    weatherViewModel: WeatherViewModel = koinViewModel()
) {
    val uiState by mapViewModel.uiState.collectAsStateWithLifecycle()
    val weatherState by weatherViewModel.uiState.collectAsStateWithLifecycle()

    MapScreenContent(
        uiState = uiState,
        weatherState = weatherState,
        onSelectPlace = mapViewModel::selectPlace,
        onStartRoute = onStartRoute,
        onOpenPlace = onOpenPlace,
        onOpenSettings = onOpenSettings,
        onOpenWeather = onOpenWeather,
        onRetryWeather = weatherViewModel::retry,
        modifier = modifier
    )
}
