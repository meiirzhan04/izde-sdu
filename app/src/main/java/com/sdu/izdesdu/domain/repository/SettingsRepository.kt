package com.sdu.izdesdu.domain.repository

import com.sdu.izdesdu.domain.model.PlaceSortOrder
import com.sdu.izdesdu.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<UserSettings>
    suspend fun setDarkTheme(enabled: Boolean)
    suspend fun setPlaceSortOrder(order: PlaceSortOrder)
}
