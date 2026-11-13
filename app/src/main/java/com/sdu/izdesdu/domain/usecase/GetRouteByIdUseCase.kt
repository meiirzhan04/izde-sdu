package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.model.Route
import com.sdu.izdesdu.domain.repository.RouteRepository

class GetRouteByIdUseCase(
    private val repository: RouteRepository
) {
    operator fun invoke(routeId: String): Route? = repository.getRouteById(routeId)
}