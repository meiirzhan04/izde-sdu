package com.sdu.izdesdu.presentation.place

import com.sdu.izdesdu.domain.model.Note
import com.sdu.izdesdu.domain.model.Place

data class PlaceUiState(
    val place: Place? = null,
    val notes: List<Note> = emptyList()
)
