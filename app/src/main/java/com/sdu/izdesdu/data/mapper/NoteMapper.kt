package com.sdu.izdesdu.data.mapper

import com.sdu.izdesdu.data.local.db.NoteEntity
import com.sdu.izdesdu.domain.model.Note

fun NoteEntity.toDomain(): Note = Note(
    id = id,
    placeId = placeId,
    text = text,
    updatedAt = updatedAt
)

fun Note.toEntity(): NoteEntity = NoteEntity(
    id = id,
    placeId = placeId,
    text = text,
    updatedAt = updatedAt
)
