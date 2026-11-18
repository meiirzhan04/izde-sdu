package com.sdu.izdesdu.presentation.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RouteScreen(
    routeId: String,
    onBack: () -> Unit,
    onStartStep: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<RouteViewModel>(key = "route_$routeId") { parametersOf(routeId) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RouteScreenContent(
        uiState = uiState,
        onBack = onBack,
        onStartStep = onStartStep,
        modifier = modifier
    )
}
