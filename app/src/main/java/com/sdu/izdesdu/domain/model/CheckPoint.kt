package com.sdu.izdesdu.domain.model

data class CheckPoint(
    val id: Int,
    val title: String,
    val instruction: String,
    val mapX: Float,
    val mapY: Float,
    val question: Question
)