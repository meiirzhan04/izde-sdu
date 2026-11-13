package com.sdu.izdesdu.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable data object MapRoute
@Serializable data object FromToRoute
@Serializable data class RouteDetailRoute(val routeId: String)
@Serializable data class QuestionRoute(val routeId: String, val step: Int)
@Serializable data class PlaceDetailRoute(val placeId: String)
@Serializable data object MyRoutesRoute
@Serializable data object ProfileRoute