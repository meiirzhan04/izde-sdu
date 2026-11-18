package com.sdu.izdesdu.presentation.map

import com.sdu.izdesdu.domain.model.Place

data class MapUiState(
    val places: List<Place> = emptyList(),
    val selectedId: String? = null,
    val totalScore: Int = 0
) {
    val selectedPlace: Place? = places.find { it.id == selectedId }
}
