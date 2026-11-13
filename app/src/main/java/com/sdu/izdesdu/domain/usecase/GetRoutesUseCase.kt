package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.model.Route
import com.sdu.izdesdu.domain.repository.RouteRepository

class GetRoutesUseCase(
    private val repository: RouteRepository
) {
    operator fun invoke(): List<Route> = repository.getRoutes()
}