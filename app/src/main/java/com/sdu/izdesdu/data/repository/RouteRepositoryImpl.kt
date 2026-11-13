package com.sdu.izdesdu.data.repository

import com.sdu.izdesdu.data.local.RouteLocalDataSource
import com.sdu.izdesdu.domain.model.Route
import com.sdu.izdesdu.domain.repository.RouteRepository

class RouteRepositoryImpl(
    private val localDataSource: RouteLocalDataSource
): RouteRepository {
    override fun getRoutes(): List<Route> = localDataSource.loadRoutes()
    override fun getRouteById(routeId: String): Route? =
        localDataSource.loadRoutes().find { it.id == routeId }
}