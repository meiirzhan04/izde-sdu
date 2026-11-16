package com.sdu.izdesdu.domain.model

data class Place(
    val id: String,
    val title: String,
    val block: String,
    val description: String,
    val nearby: List<String>,
    val questionsCount: Int,
    val colorHex: Long,
    val mapX: Float,
    val mapY: Float
)
