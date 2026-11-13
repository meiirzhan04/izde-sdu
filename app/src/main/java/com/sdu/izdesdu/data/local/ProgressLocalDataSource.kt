package com.sdu.izdesdu.data.local

import com.sdu.izdesdu.domain.model.UserProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProgressLocalDataSource {
    private val _progress = MutableStateFlow(UserProgress())
    val progress: StateFlow<UserProgress> = _progress.asStateFlow()

    fun addAnswerResult(isCorrect: Boolean, earnedPoints: Int) {
        _progress.update { current ->
            current.copy(
                totalScore = current.totalScore + earnedPoints,
                answeredCount = current.answeredCount + 1,
                correctCount = current.correctCount + if (isCorrect) 1 else 0
            )
        }
    }

    fun markRouteCompleted(routeId: String) {
        _progress.update { current ->
            current.copy(completedRouteIds = current.completedRouteIds + routeId)
        }
    }
}