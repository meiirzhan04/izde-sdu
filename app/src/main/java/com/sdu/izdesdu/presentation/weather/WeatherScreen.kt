package com.sdu.izdesdu.presentation.weather

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WeatherScreen(
    onBack: () -> Unit
) {
    val viewModel = koinViewModel<WeatherViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WeatherScreenContent(
        uiState = uiState,
        onBack = onBack,
        onRetry = viewModel::retry
    )
}
