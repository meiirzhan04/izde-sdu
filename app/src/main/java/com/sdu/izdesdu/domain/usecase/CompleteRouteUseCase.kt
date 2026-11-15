package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.repository.ProgressRepository
import com.sdu.izdesdu.domain.repository.RouteRepository

class CompleteRouteUseCase(
    private val repository: ProgressRepository,
    private val routes: RouteRepository
) {
    operator fun invoke(routeId: String): Boolean {
        val route = routes.getRouteById(routeId) ?: return false
        if (route.checkpoints.isEmpty()) return false
        val answers = repository.progress.value.routeProgress[routeId]?.answers.orEmpty()
        if (route.checkpoints.any { answers[it.id]?.isCorrect != true }) return false
        repository.markRouteCompleted(routeId)
        return true
    }
}
