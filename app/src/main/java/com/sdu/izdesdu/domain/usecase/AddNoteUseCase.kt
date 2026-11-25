package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.repository.NoteRepository

class AddNoteUseCase(
    val repository: NoteRepository
) {
    suspend operator fun invoke(placeId: String, text: String) {
        val cleanText = text.trim()
        if (cleanText.isEmpty()) return
        repository.addNote(placeId, cleanText)
    }
}
