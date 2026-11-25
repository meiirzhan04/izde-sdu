package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.model.Note
import com.sdu.izdesdu.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeNoteRepository : NoteRepository {
    val notes = MutableStateFlow<List<Note>>(emptyList())
    var nextId = 1L

    override fun observeNotes(placeId: String): Flow<List<Note>> =
        notes.map { list -> list.filter { it.placeId == placeId } }

    override suspend fun addNote(placeId: String, text: String) {
        notes.value = notes.value + Note(id = nextId++, placeId = placeId, text = text, updatedAt = 0)
    }

    override suspend fun updateNote(note: Note) {
        notes.value = notes.value.map { if (it.id == note.id) note else it }
    }

    override suspend fun deleteNote(note: Note) {
        notes.value = notes.value.filterNot { it.id == note.id }
    }
}
