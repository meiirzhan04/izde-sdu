package com.sdu.izdesdu.data.repository

import com.sdu.izdesdu.data.local.SettingsDataStore
import com.sdu.izdesdu.domain.model.PlaceSortOrder
import com.sdu.izdesdu.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val store: SettingsDataStore) : SettingsRepository {
    override val settings = store.settings
    override suspend fun setDarkTheme(enabled: Boolean) = store.setDarkTheme(enabled)
    override suspend fun setPlaceSortOrder(order: PlaceSortOrder) = store.setPlaceSortOrder(order)
}
