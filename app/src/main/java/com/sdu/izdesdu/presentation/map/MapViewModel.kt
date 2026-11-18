package com.sdu.izdesdu.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdu.izdesdu.domain.usecase.GetPlacesUseCase
import com.sdu.izdesdu.domain.usecase.GetUserProgressUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapViewModel(
    getPlacesUseCase: GetPlacesUseCase,
    getUserProgressUseCase: GetUserProgressUseCase
) : ViewModel() {
    private val initialPlaces = getPlacesUseCase()
    private val userProgress = getUserProgressUseCase()

    private val _uiState = MutableStateFlow(
        MapUiState(
            places = initialPlaces,
            selectedId = initialPlaces.find { it.id == "canteen" }?.id
                ?: initialPlaces.firstOrNull()?.id,
            totalScore = userProgress.value.totalScore
        )
    )
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userProgress.collect { progress ->
                _uiState.update { it.copy(totalScore = progress.totalScore) }
            }
        }
    }

    fun selectPlace(id: String) {
        _uiState.update { it.copy(selectedId = id) }
    }
}
