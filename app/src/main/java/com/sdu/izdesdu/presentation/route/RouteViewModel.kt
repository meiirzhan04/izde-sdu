package com.sdu.izdesdu.presentation.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sdu.izdesdu.di.AppContainer
import com.sdu.izdesdu.domain.usecase.GetRouteByIdUseCase
import com.sdu.izdesdu.domain.usecase.GetUserProgressUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RouteViewModel(
    private val routeId: String,
    private val getRouteByIdUseCase: GetRouteByIdUseCase,
    private val getUserProgressUseCase: GetUserProgressUseCase
) : ViewModel() {
    private val route = getRouteByIdUseCase(routeId)
    private val initialProgress = getUserProgressUseCase().value
    private val _uiState = MutableStateFlow(
        RouteUiState(
            route = route,
            currentStep = (initialProgress.routeSteps[routeId] ?: 1)
                .coerceIn(1, route?.checkpoints?.size?.coerceAtLeast(1) ?: 1),
            isCompleted = routeId in initialProgress.completedRouteIds
        )
    )
    val uiState: StateFlow<RouteUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getUserProgressUseCase().collect { progress ->
                val checkpointCount = _uiState.value.route?.checkpoints?.size ?: 0
                val step = if (checkpointCount > 0) {
                    (progress.routeSteps[routeId] ?: 1).coerceIn(1, checkpointCount)
                } else {
                    1
                }
                _uiState.update {
                    it.copy(
                        currentStep = step,
                        isCompleted = routeId in progress.completedRouteIds
                    )
                }
            }
        }
    }
    companion object {
        fun factory(routeId: String, container: AppContainer): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RouteViewModel(
                        routeId = routeId,
                        getRouteByIdUseCase = container.getRouteByIdUseCase,
                        getUserProgressUseCase = container.getUserProgressUseCase
                    ) as T
                }
            }
    }
}
