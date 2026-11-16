package com.sdu.izdesdu.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sdu.izdesdu.di.AppContainer
import com.sdu.izdesdu.domain.usecase.GetPlacesUseCase
import com.sdu.izdesdu.domain.usecase.GetUserProgressUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapViewModel(
    getPlacesUseCase: GetPlacesUseCase,
    private val getUserProgressUseCase: GetUserProgressUseCase
) : ViewModel() {
    private val initialPlaces = getPlacesUseCase()
    private val defaultPlaceId = initialPlaces.find { it.id == "canteen" }?.id
        ?: initialPlaces.firstOrNull()?.id

    private val _uiState = MutableStateFlow(
        MapUiState(
            places = initialPlaces,
            selectedId = defaultPlaceId,
            totalScore = getUserProgressUseCase().value.totalScore
        )
    )
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getUserProgressUseCase().collect { progress ->
                _uiState.update { it.copy(totalScore = progress.totalScore) }
            }
        }
    }

    fun selectPlace(id: String) {
        _uiState.update { it.copy(selectedId = id) }
    }

    companion object {
        fun factory(container: AppContainer): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MapViewModel(
                        getPlacesUseCase = container.getPlacesUseCase,
                        getUserProgressUseCase = container.getUserProgressUseCase
                    ) as T
                }
            }
    }
}
