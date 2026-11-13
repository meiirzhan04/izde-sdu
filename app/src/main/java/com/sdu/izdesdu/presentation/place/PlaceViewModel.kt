package com.sdu.izdesdu.presentation.place

import androidx.lifecycle.ViewModel
import com.sdu.izdesdu.data.repository.PlaceRepositoryImpl
import com.sdu.izdesdu.domain.model.Place
import com.sdu.izdesdu.domain.repository.PlaceRepository

class PlaceViewModel(
    val repository: PlaceRepository = PlaceRepositoryImpl()
) : ViewModel() {

    fun getPlace(id: String): Place? {
        return repository.getPlace(id)
    }
}