package com.sdu.izdesdu.presentation.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(onBack: () -> Unit, viewModel: SettingsViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreenContent(
        settings = state.settings,
        onBack = onBack,
        onDarkThemeChanged = viewModel::onDarkThemeChanged,
        onSortOrderSelected = viewModel::onSortOrderSelected
    )
}
