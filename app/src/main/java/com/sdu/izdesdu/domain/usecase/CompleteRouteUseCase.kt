package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.repository.ProgressRepository

class CompleteRouteUseCase(
    private val repository: ProgressRepository
) {
    operator fun invoke(routeId: String) {
        repository.markRouteCompleted(routeId)
    }
}