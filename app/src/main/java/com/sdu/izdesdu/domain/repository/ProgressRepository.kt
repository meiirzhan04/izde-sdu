package com.sdu.izdesdu.domain.repository

import com.sdu.izdesdu.domain.model.UserProgress
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {
    val progress: StateFlow<UserProgress>
    fun addAnswerResult(isCorrect: Boolean, earnedPoints: Int)
    fun markRouteCompleted(routeId: String)
}