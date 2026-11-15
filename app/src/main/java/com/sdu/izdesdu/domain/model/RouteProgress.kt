package com.sdu.izdesdu.domain.model

data class RouteProgress(
    val answers: Map<Int, CheckpointProgress> = emptyMap()
) {
    val earnedPoints: Int get() = answers.values.sumOf { it.earnedPoints }
    val mistakes: Int get() = answers.values.sumOf { it.mistakes }
    val completedCheckpoints: Int get() = answers.values.count { it.isCorrect }
}
