package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.model.CheckpointProgress
import com.sdu.izdesdu.domain.repository.ProgressRepository
import com.sdu.izdesdu.domain.repository.RouteRepository

class SubmitRouteAnswerUseCase(
    private val routes: RouteRepository,
    private val progress: ProgressRepository,
    private val checkAnswer: CheckAnswerUseCase
) {
    operator fun invoke(routeId: String, step: Int, selectedIndex: Int): CheckpointProgress? {
        val route = routes.getRouteById(routeId) ?: return null
        val checkpoint = route.checkpoints.getOrNull(step - ONE) ?: return null
        if (selectedIndex !in checkpoint.question.options.indices) return null
        val answers = progress.progress.value.routeProgress[routeId]?.answers.orEmpty()
        if (route.checkpoints.take(step - ONE).any { answers[it.id]?.isCorrect != true }) return null
        if (answers[checkpoint.id]?.isCorrect == true) return answers[checkpoint.id]
        val result = checkAnswer(checkpoint.question, selectedIndex)
        progress.recordRouteAnswer(routeId, checkpoint.id, selectedIndex, result)
        if (result.isCorrect) {
            progress.setRouteStep(routeId, (step + ONE).coerceAtMost(route.checkpoints.size))
        }
        return progress.progress.value.routeProgress[routeId]?.answers?.get(checkpoint.id)
    }

    companion object {
        const val ONE = 1
    }
}
