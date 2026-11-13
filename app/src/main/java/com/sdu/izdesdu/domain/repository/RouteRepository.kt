package com.sdu.izdesdu.domain.repository

import com.sdu.izdesdu.domain.model.Route

interface RouteRepository {
    fun getRoutes(): List<Route>
    fun getRouteById(routeId: String): Route?
}