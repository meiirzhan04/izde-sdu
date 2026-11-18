package com.sdu.izdesdu.presentation.place

import androidx.lifecycle.ViewModel
import com.sdu.izdesdu.domain.usecase.GetPlaceByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlaceViewModel(
    placeId: String,
    getPlaceByIdUseCase: GetPlaceByIdUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlaceUiState(place = getPlaceByIdUseCase(placeId)))
    val uiState: StateFlow<PlaceUiState> = _uiState.asStateFlow()
}
