package com.sdu.izdesdu.domain.repository

import com.sdu.izdesdu.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun observeNotes(placeId: String): Flow<List<Note>>
    suspend fun addNote(placeId: String, text: String)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
}
