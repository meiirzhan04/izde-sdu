package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.model.Note
import com.sdu.izdesdu.domain.repository.NoteRepository

class UpdateNoteUseCase(
    val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note, newText: String) {
        val cleanText = newText.trim()
        if (cleanText.isEmpty() || cleanText == note.text) return
        repository.updateNote(note.copy(text = cleanText))
    }
}
