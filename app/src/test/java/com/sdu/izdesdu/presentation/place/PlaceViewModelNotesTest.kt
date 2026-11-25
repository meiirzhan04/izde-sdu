package com.sdu.izdesdu.presentation.place

import com.sdu.izdesdu.domain.model.Place
import com.sdu.izdesdu.domain.repository.PlaceRepository
import com.sdu.izdesdu.domain.usecase.AddNoteUseCase
import com.sdu.izdesdu.domain.usecase.DeleteNoteUseCase
import com.sdu.izdesdu.domain.usecase.FakeNoteRepository
import com.sdu.izdesdu.domain.usecase.GetPlaceByIdUseCase
import com.sdu.izdesdu.domain.usecase.GetPlaceNotesUseCase
import com.sdu.izdesdu.domain.usecase.UpdateNoteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlaceViewModelNotesTest {
    val library = Place(
        id = "library",
        title = "Библиотека",
        block = "Блок B",
        description = "",
        nearby = emptyList(),
        questionsCount = 3,
        colorHex = 0xFF2F66D8,
        mapX = 105.6f,
        mapY = 741.2f
    )

    val placeRepository = object : PlaceRepository {
        override fun getPlaces(): List<Place> = listOf(library)
        override fun getPlace(id: String): Place? = listOf(library).find { it.id == id }
    }

    val noteRepository = FakeNoteRepository()

    fun createViewModel() = PlaceViewModel(
        placeId = "library",
        getPlaceByIdUseCase = GetPlaceByIdUseCase(placeRepository),
        getPlaceNotesUseCase = GetPlaceNotesUseCase(noteRepository),
        addNoteUseCase = AddNoteUseCase(noteRepository),
        updateNoteUseCase = UpdateNoteUseCase(noteRepository),
        deleteNoteUseCase = DeleteNoteUseCase(noteRepository),
        dispatcher = Dispatchers.Unconfined
    )

    @Test
    fun init_loadsPlaceAndSavedNotes() {
        runBlocking { noteRepository.addNote("library", "Уже сохранённая заметка") }

        val viewModel = createViewModel()

        assertEquals("Библиотека", viewModel.uiState.value.place?.title)
        assertEquals(listOf("Уже сохранённая заметка"), viewModel.uiState.value.notes.map { it.text })
    }

    @Test
    fun addNote_appearsInUiState() {
        val viewModel = createViewModel()

        viewModel.addNote("Тихий зал на 2 этаже")

        assertEquals(listOf("Тихий зал на 2 этаже"), viewModel.uiState.value.notes.map { it.text })
    }

    @Test
    fun updateNote_changesTextInUiState() {
        val viewModel = createViewModel()
        viewModel.addNote("Старый текст")
        val note = viewModel.uiState.value.notes.single()

        viewModel.updateNote(note, "Новый текст")

        assertEquals("Новый текст", viewModel.uiState.value.notes.single().text)
    }

    @Test
    fun deleteNote_removesNoteFromUiState() {
        val viewModel = createViewModel()
        viewModel.addNote("Удалить меня")
        val note = viewModel.uiState.value.notes.single()

        viewModel.deleteNote(note)

        assertTrue(viewModel.uiState.value.notes.isEmpty())
    }
}
