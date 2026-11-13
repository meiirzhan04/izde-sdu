package com.sdu.izdesdu.domain.repository

import com.sdu.izdesdu.domain.model.Place

interface PlaceRepository {
    fun getPlaces(): List<Place>
    fun getPlace(id: String): Place?
}