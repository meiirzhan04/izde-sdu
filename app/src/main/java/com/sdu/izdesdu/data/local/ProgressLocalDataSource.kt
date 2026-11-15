package com.sdu.izdesdu.data.local

import com.sdu.izdesdu.domain.model.AnswerResult
import com.sdu.izdesdu.domain.model.CheckpointProgress
import com.sdu.izdesdu.domain.model.RouteProgress
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

    fun recordRouteAnswer(
        routeId: String,
        checkpointId: Int,
        selectedIndex: Int,
        result: AnswerResult
    ) {
        _progress.update { current ->
            val route = current.routeProgress[routeId] ?: RouteProgress()
            val previous = route.answers[checkpointId]
            if (previous?.isCorrect == true || routeId in current.completedRouteIds) {
                return@update current
            }
            val answer = CheckpointProgress(
                selectedIndex = selectedIndex,
                isCorrect = result.isCorrect,
                earnedPoints = result.earnedPoints,
                mistakes = (previous?.mistakes ?: 0) + if (result.isCorrect) 0 else 1
            )
            current.copy(
                totalScore = current.totalScore + result.earnedPoints,
                answeredCount = current.answeredCount + 1,
                correctCount = current.correctCount + if (result.isCorrect) 1 else 0,
                routeProgress = current.routeProgress + (
                    routeId to route.copy(answers = route.answers + (checkpointId to answer))
                )
            )
        }
    }

    fun setRouteStep(routeId: String, step: Int) {
        val safeStep = step.coerceAtLeast(1)
        _progress.update { current ->
            current.copy(routeSteps = current.routeSteps + (routeId to maxOf(safeStep, current.routeSteps[routeId] ?: 1)))
        }
    }

    fun markRouteCompleted(routeId: String) {
        _progress.update { current ->
            current.copy(completedRouteIds = current.completedRouteIds + routeId)
        }
    }
}
