package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.repository.ProgressRepository

class UpdateRouteStepUseCase(
    private val repository: ProgressRepository
) {
    operator fun invoke(routeId: String, step: Int) {
        repository.setRouteStep(routeId, step)
    }
}
