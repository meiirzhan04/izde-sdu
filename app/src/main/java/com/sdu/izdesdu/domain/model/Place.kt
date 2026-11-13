package com.sdu.izdesdu.domain.model

import androidx.compose.ui.graphics.Color

data class Place(
    val id: String,
    val title: String,
    val block: String,
    val description: String,
    val nearby: List<String>,
    val questionsCount: Int,
    val color: Color,
    val mapX: Float,
    val mapY: Float
)