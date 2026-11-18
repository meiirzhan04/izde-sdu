package com.sdu.izdesdu.presentation.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MapScreen(
    onStartRoute: (String) -> Unit,
    onOpenPlace: (String) -> Unit
) {
    val viewModel = koinViewModel<MapViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MapScreenContent(
        uiState = uiState,
        onSelectPlace = viewModel::selectPlace,
        onStartRoute = onStartRoute,
        onOpenPlace = onOpenPlace
    )
}
