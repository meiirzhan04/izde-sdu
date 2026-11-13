package com.sdu.izdesdu.presentation.map

import androidx.lifecycle.ViewModel
import com.sdu.izdesdu.data.repository.PlaceRepositoryImpl
import com.sdu.izdesdu.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MapViewModel(
    private val repository: PlaceRepository = PlaceRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MapUiState(
            places = repository.getPlaces(),
            selectedId = "canteen"
        )
    )

    val uiState: StateFlow<MapUiState> = _uiState

    fun selectPlace(id: String) {
        _uiState.value = _uiState.value.copy(selectedId = id)
    }
}