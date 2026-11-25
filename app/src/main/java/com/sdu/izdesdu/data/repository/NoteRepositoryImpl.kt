package com.sdu.izdesdu.data.repository

import com.sdu.izdesdu.data.local.db.NoteDao
import com.sdu.izdesdu.data.local.db.NoteEntity
import com.sdu.izdesdu.data.mapper.toDomain
import com.sdu.izdesdu.data.mapper.toEntity
import com.sdu.izdesdu.domain.model.Note
import com.sdu.izdesdu.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val currentTime: () -> Long = { System.currentTimeMillis() }
) : NoteRepository {

    override fun observeNotes(placeId: String): Flow<List<Note>> =
        noteDao.observeNotesForPlace(placeId).map { notes ->
            notes.map { it.toDomain() }
        }

    override suspend fun addNote(placeId: String, text: String) {
        noteDao.insert(
            NoteEntity(
                placeId = placeId,
                text = text,
                updatedAt = currentTime()
            )
        )
    }

    override suspend fun updateNote(note: Note) {
        noteDao.update(note.copy(updatedAt = currentTime()).toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.delete(note.toEntity())
    }
}
