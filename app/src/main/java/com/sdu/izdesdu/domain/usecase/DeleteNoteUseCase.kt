package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.model.Note
import com.sdu.izdesdu.domain.repository.NoteRepository

class DeleteNoteUseCase(
    val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}
