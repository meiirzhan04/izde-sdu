package com.sdu.izdesdu.data.repository

import com.sdu.izdesdu.data.local.PlaceLocalDataSource
import com.sdu.izdesdu.domain.model.Place
import com.sdu.izdesdu.domain.repository.PlaceRepository

class PlaceRepositoryImpl(
    private val localDataSource: PlaceLocalDataSource = PlaceLocalDataSource()
) : PlaceRepository {
    override fun getPlaces(): List<Place> = localDataSource.loadPlaces()

    override fun getPlace(id: String): Place? =
        localDataSource.loadPlaces().find { it.id == id }
}
