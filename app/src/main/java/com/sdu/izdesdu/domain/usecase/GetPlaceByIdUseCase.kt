package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.model.Place
import com.sdu.izdesdu.domain.repository.PlaceRepository

class GetPlaceByIdUseCase(
    private val repository: PlaceRepository
) {
    operator fun invoke(placeId: String): Place? = repository.getPlace(placeId)
}
