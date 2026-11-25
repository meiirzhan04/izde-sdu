package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.model.PlaceSortOrder
import com.sdu.izdesdu.domain.repository.SettingsRepository

class SetPlaceSortOrderUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(order: PlaceSortOrder) = repository.setPlaceSortOrder(order)
}
