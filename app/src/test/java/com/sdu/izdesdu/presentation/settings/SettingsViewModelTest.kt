package com.sdu.izdesdu.presentation.settings

import com.sdu.izdesdu.domain.model.PlaceSortOrder
import com.sdu.izdesdu.domain.model.UserSettings
import com.sdu.izdesdu.domain.usecase.GetUserSettingsUseCase
import com.sdu.izdesdu.domain.usecase.SetDarkThemeUseCase
import com.sdu.izdesdu.domain.usecase.SetPlaceSortOrderUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    @Test
    fun changingOnePreferencePreservesTheOtherAndNewViewModelsReadBoth() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val repository = FakeSettingsRepository()
            fun openSettings() = SettingsViewModel(
                GetUserSettingsUseCase(repository),
                SetDarkThemeUseCase(repository),
                SetPlaceSortOrderUseCase(repository)
            )
            val model = openSettings()
            runCurrent()
            assertEquals(UserSettings(), model.uiState.value.settings)
            model.onDarkThemeChanged(true)
            model.onSortOrderSelected(PlaceSortOrder.TITLE_ASC)
            runCurrent()
            val expected = UserSettings(true, PlaceSortOrder.TITLE_ASC)
            assertEquals(expected, repository.settings.value)
            val reopened = openSettings()
            runCurrent()
            assertEquals(expected, reopened.uiState.value.settings)
            reopened.onDarkThemeChanged(false)
            runCurrent()
            assertEquals(expected.copy(isDarkTheme = false), model.uiState.value.settings)
        } finally {
            Dispatchers.resetMain()
        }
    }
}
