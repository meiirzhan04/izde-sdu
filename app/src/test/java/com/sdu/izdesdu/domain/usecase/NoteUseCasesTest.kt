package com.sdu.izdesdu.domain.usecase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NoteUseCasesTest {
    val repository = FakeNoteRepository()
    val getPlaceNotes = GetPlaceNotesUseCase(repository)
    val addNote = AddNoteUseCase(repository)
    val updateNote = UpdateNoteUseCase(repository)
    val deleteNote = DeleteNoteUseCase(repository)

    @Test
    fun addNote_savesTrimmedText() = runBlocking {
        addNote("library", "  Тихий зал на 2 этаже  ")

        val notes = getPlaceNotes("library").first()
        assertEquals(1, notes.size)
        assertEquals("Тихий зал на 2 этаже", notes[0].text)
    }

    @Test
    fun addNote_ignoresBlankText() = runBlocking {
        addNote("library", "   ")

        assertTrue(getPlaceNotes("library").first().isEmpty())
    }

    @Test
    fun getPlaceNotes_returnsOnlyNotesOfThatPlace() = runBlocking {
        addNote("library", "Выдача книг до 18:00")
        addNote("canteen", "Кофе дешевле до 10:00")

        val notes = getPlaceNotes("canteen").first()
        assertEquals(listOf("Кофе дешевле до 10:00"), notes.map { it.text })
    }

    @Test
    fun updateNote_changesText() = runBlocking {
        addNote("library", "Старый текст")
        val note = getPlaceNotes("library").first().single()

        updateNote(note, "Новый текст")

        assertEquals("Новый текст", getPlaceNotes("library").first().single().text)
    }

    @Test
    fun updateNote_ignoresBlankText() = runBlocking {
        addNote("library", "Текст")
        val note = getPlaceNotes("library").first().single()

        updateNote(note, "   ")

        assertEquals("Текст", getPlaceNotes("library").first().single().text)
    }

    @Test
    fun deleteNote_removesOnlyThatNote() = runBlocking {
        addNote("library", "Первая")
        addNote("library", "Вторая")
        val first = getPlaceNotes("library").first().first { it.text == "Первая" }

        deleteNote(first)

        assertEquals(listOf("Вторая"), getPlaceNotes("library").first().map { it.text })
    }
}
