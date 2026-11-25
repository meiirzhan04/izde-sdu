package com.sdu.izdesdu.presentation.place

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PlaceScreen(
    placeId: String,
    onBack: () -> Unit,
    onStartRoute: (String) -> Unit
) {
    val viewModel = koinViewModel<PlaceViewModel>(key = "place_$placeId") {
        parametersOf(placeId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PlaceScreenContent(
        uiState = uiState,
        onBack = onBack,
        onStartRoute = onStartRoute,
        onAddNote = viewModel::addNote,
        onUpdateNote = viewModel::updateNote,
        onDeleteNote = viewModel::deleteNote
    )
}
