package com.sdu.izdesdu.data.repository

import com.sdu.izdesdu.data.local.ProgressLocalDataSource
import com.sdu.izdesdu.domain.model.AnswerResult
import com.sdu.izdesdu.domain.model.UserProgress
import com.sdu.izdesdu.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.StateFlow

class ProgressRepositoryImpl(
    private val localDataSource: ProgressLocalDataSource
): ProgressRepository {
    override val progress: StateFlow<UserProgress> = localDataSource.progress
    override fun addAnswerResult(isCorrect: Boolean, earnedPoints: Int) {
        localDataSource.addAnswerResult(isCorrect, earnedPoints)
    }
    override fun recordRouteAnswer(
        routeId: String,
        checkpointId: Int,
        selectedIndex: Int,
        result: AnswerResult
    ) {
        localDataSource.recordRouteAnswer(routeId, checkpointId, selectedIndex, result)
    }
    override fun setRouteStep(routeId: String, step: Int) {
        localDataSource.setRouteStep(routeId, step)
    }
    override fun markRouteCompleted(routeId: String) {
        localDataSource.markRouteCompleted(routeId)
    }
}
