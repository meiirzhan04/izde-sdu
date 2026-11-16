package com.sdu.izdesdu.domain.repository

import com.sdu.izdesdu.domain.model.AnswerResult
import com.sdu.izdesdu.domain.model.UserProgress
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {
    val progress: StateFlow<UserProgress>
    fun recordRouteAnswer(routeId: String, checkpointId: Int, selectedIndex: Int, result: AnswerResult)
    fun setRouteStep(routeId: String, step: Int)
    fun markRouteCompleted(routeId: String)
}
