package com.sdu.izdesdu.presentation.route

import com.sdu.izdesdu.domain.model.Route

data class RouteUiState(
    val route: Route? = null,
    val currentStep: Int = 1,
    val isCompleted: Boolean = false
)