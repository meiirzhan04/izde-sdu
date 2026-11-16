package com.sdu.izdesdu.presentation.place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sdu.izdesdu.di.AppContainer
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

    companion object {
        fun factory(placeId: String, container: AppContainer): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlaceViewModel(
                        placeId = placeId,
                        getPlaceByIdUseCase = container.getPlaceByIdUseCase
                    ) as T
                }
            }
    }
}
