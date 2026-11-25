package com.sdu.izdesdu.presentation.map

import com.sdu.izdesdu.data.local.ProgressLocalDataSource
import com.sdu.izdesdu.data.repository.ProgressRepositoryImpl
import com.sdu.izdesdu.domain.model.Place
import com.sdu.izdesdu.domain.model.PlaceSortOrder
import com.sdu.izdesdu.domain.repository.PlaceRepository
import com.sdu.izdesdu.domain.usecase.GetPlacesUseCase
import com.sdu.izdesdu.domain.usecase.GetUserProgressUseCase
import com.sdu.izdesdu.domain.usecase.GetUserSettingsUseCase
import com.sdu.izdesdu.presentation.settings.FakeSettingsRepository
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
class MapViewModelSortTest {
    @Test
    fun allOrdersPreserveSelectionAndDefaultRestoresSourceOrder() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val gym = place("Зал", 2)
            val canteen = place("Столовая", 1)
            val library = place("Библиотека", 2)
            val original = listOf(gym, canteen, library)
            val places = object : PlaceRepository {
                override fun getPlaces() = original
                override fun getPlace(id: String) = original.find { it.id == id }
            }
            val settings = FakeSettingsRepository()
            settings.setPlaceSortOrder(PlaceSortOrder.QUESTIONS_ASC)
            val model = MapViewModel(
                GetPlacesUseCase(places),
                GetUserProgressUseCase(ProgressRepositoryImpl(ProgressLocalDataSource())),
                GetUserSettingsUseCase(settings)
            )
            runCurrent()
            assertEquals(listOf(canteen, gym, library), model.uiState.value.places)
            assertEquals(PlaceSortOrder.QUESTIONS_ASC, model.uiState.value.sortOrder)
            assertEquals(gym, model.uiState.value.selectedPlace)
            model.selectPlace(library.id)

            suspend fun check(order: PlaceSortOrder, expected: List<Place>) {
                settings.setPlaceSortOrder(order)
                runCurrent()
                assertEquals(order, model.uiState.value.sortOrder)
                assertEquals(expected, model.uiState.value.places)
                assertEquals(library, model.uiState.value.selectedPlace)
            }
            check(PlaceSortOrder.TITLE_ASC, listOf(library, gym, canteen))
            check(PlaceSortOrder.QUESTIONS_ASC, listOf(canteen, gym, library))
            check(PlaceSortOrder.QUESTIONS_DESC, listOf(gym, library, canteen))
            check(PlaceSortOrder.DEFAULT, original)
        } finally {
            Dispatchers.resetMain()
        }
    }

    private fun place(title: String, questions: Int) = Place(
        id = title, title = title, block = "", description = "", nearby = emptyList(),
        questionsCount = questions, colorHex = 0L, mapX = 0f, mapY = 0f
    )
}
