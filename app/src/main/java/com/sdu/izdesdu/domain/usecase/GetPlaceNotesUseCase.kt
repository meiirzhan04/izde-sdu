package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.model.Note
import com.sdu.izdesdu.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetPlaceNotesUseCase(
    val repository: NoteRepository
) {
    operator fun invoke(placeId: String): Flow<List<Note>> = repository.observeNotes(placeId)
}
