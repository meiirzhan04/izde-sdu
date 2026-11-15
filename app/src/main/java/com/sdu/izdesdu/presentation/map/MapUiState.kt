package com.sdu.izdesdu.presentation.map

import com.sdu.izdesdu.domain.model.Place

data class MapUiState(
    val places: List<Place>,
    val selectedId: String?
) {
    val selectedPlace: Place?
        get() = places.find { it.id == selectedId }
}