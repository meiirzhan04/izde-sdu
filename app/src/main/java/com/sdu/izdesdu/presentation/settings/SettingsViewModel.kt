package com.sdu.izdesdu.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdu.izdesdu.domain.model.PlaceSortOrder
import com.sdu.izdesdu.domain.usecase.GetUserSettingsUseCase
import com.sdu.izdesdu.domain.usecase.SetDarkThemeUseCase
import com.sdu.izdesdu.domain.usecase.SetPlaceSortOrderUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    getSettings: GetUserSettingsUseCase,
    private val setDarkTheme: SetDarkThemeUseCase,
    private val setPlaceSortOrder: SetPlaceSortOrderUseCase
) : ViewModel() {
    val uiState = getSettings().map(::SettingsUiState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, SettingsUiState())

    fun onDarkThemeChanged(enabled: Boolean) {
        viewModelScope.launch { setDarkTheme(enabled) }
    }

    fun onSortOrderSelected(order: PlaceSortOrder) {
        viewModelScope.launch { setPlaceSortOrder(order) }
    }
}
