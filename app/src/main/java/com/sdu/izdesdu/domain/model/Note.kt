package com.sdu.izdesdu.domain.model

data class Note(
    val id: Long,
    val placeId: String,
    val text: String,
    val updatedAt: Long
)
