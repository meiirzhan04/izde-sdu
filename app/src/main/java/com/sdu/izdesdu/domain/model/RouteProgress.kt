package com.sdu.izdesdu.domain.model

data class RouteProgress(
    val answers: Map<Int, CheckpointProgress> = emptyMap()
) {
    val earnedPoints: Int = answers.values.sumOf { it.earnedPoints }
    val mistakes: Int = answers.values.sumOf { it.mistakes }
    val completedCheckpoints: Int = answers.values.count { it.isCorrect }
}
