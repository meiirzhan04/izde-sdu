package com.sdu.izdesdu.presentation.settings

import com.sdu.izdesdu.domain.model.PlaceSortOrder
import com.sdu.izdesdu.domain.model.UserSettings
import com.sdu.izdesdu.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

internal class FakeSettingsRepository : SettingsRepository {
    override val settings = MutableStateFlow(UserSettings())

    override suspend fun setDarkTheme(enabled: Boolean) {
        settings.update { it.copy(isDarkTheme = enabled) }
    }

    override suspend fun setPlaceSortOrder(order: PlaceSortOrder) {
        settings.update { it.copy(placeSortOrder = order) }
    }
}
