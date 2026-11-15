package com.sdu.izdesdu.domain.model

data class CheckpointProgress(
    val selectedIndex: Int,
    val isCorrect: Boolean,
    val earnedPoints: Int = 0,
    val mistakes: Int = 0
)
