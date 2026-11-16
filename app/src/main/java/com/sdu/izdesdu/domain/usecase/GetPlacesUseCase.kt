package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.model.Place
import com.sdu.izdesdu.domain.repository.PlaceRepository

class GetPlacesUseCase(
    private val repository: PlaceRepository
) {
    operator fun invoke(): List<Place> = repository.getPlaces()
}
