package com.sdu.izdesdu.domain.model

data class Route(
    val id: String,
    val title: String,
    val fromPlaceId: String,
    val toPlaceId: String,
    val checkpoints: List<CheckPoint>
)