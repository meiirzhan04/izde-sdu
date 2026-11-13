package com.sdu.izdesdu.domain.model

data class UserProgress(
    val totalScore: Int = 0,
    val answeredCount: Int = 0,
    val correctCount: Int = 0,
    val completedRouteIds: Set<String> = emptySet()
)