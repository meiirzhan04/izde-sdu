package com.sdu.izdesdu.presentation.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdu.izdesdu.domain.model.UserProgress
import com.sdu.izdesdu.domain.usecase.CompleteRouteUseCase
import com.sdu.izdesdu.domain.usecase.GetRouteByIdUseCase
import com.sdu.izdesdu.domain.usecase.GetUserProgressUseCase
import com.sdu.izdesdu.domain.usecase.SubmitRouteAnswerUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

class QuestionViewModel(
    private val routeId: String,
    private val step: Int,
    getRouteByIdUseCase: GetRouteByIdUseCase,
    private val getUserProgressUseCase: GetUserProgressUseCase,
    private val submitRouteAnswerUseCase: SubmitRouteAnswerUseCase,
    private val completeRouteUseCase: CompleteRouteUseCase,
    dispatcher: CoroutineDispatcher? = null
) : ViewModel() {
    private val route = getRouteByIdUseCase(routeId)
    private val checkpoint = route?.checkpoints?.getOrNull(step - 1)
    private val _uiState = MutableStateFlow(
        QuestionUiState(
            route = route,
            checkpoint = checkpoint,
            checkpointNumber = step,
            totalCheckpoints = route?.checkpoints?.size ?: 0
        )
    )
    val uiState: StateFlow<QuestionUiState> = _uiState.asStateFlow()

    init {
        updateProgress(getUserProgressUseCase().value)
        viewModelScope.launch(dispatcher ?: EmptyCoroutineContext) {
            getUserProgressUseCase().collect(::updateProgress)
        }
    }

    private fun updateProgress(progress: UserProgress) {
        val routeProgress = progress.routeProgress[routeId]
        val answer = routeProgress?.answers?.get(checkpoint?.id)
        val locked = route?.checkpoints.orEmpty().take((step - 1).coerceAtLeast(0))
            .any { routeProgress?.answers?.get(it.id)?.isCorrect != true }
        _uiState.update { state ->
            state.copy(
                selectedIndex = answer?.selectedIndex,
                isCorrect = answer?.isCorrect,
                showHint = state.showHint || answer?.isCorrect == false,
                totalScore = progress.totalScore,
                routeEarnedPoints = routeProgress?.earnedPoints ?: 0,
                mistakes = routeProgress?.mistakes ?: 0,
                isLocked = locked,
                isRouteFinished = routeId in progress.completedRouteIds
            )
        }
    }

    fun selectAnswer(index: Int) {
        if (_uiState.value.isRouteFinished) return
        submitRouteAnswerUseCase(routeId, step, index) ?: return
        updateProgress(getUserProgressUseCase().value)
    }

    fun showHint() {
        val state = _uiState.value
        if (state.isMissing || state.isLocked || state.isCorrect == true) return
        _uiState.update { it.copy(showHint = !it.showHint) }
    }

    fun finishCurrentStep(): StepAction {
        updateProgress(getUserProgressUseCase().value)
        val current = _uiState.value
        if (current.isMissing || current.isLocked || current.isCorrect != true) return StepAction.Blocked
        if (!current.isLastStep) return StepAction.Next(step + 1)
        if (!completeRouteUseCase(routeId)) return StepAction.Blocked
        updateProgress(getUserProgressUseCase().value)
        return StepAction.Finished
    }
}

sealed interface StepAction {
    data class Next(val step: Int) : StepAction
    data object Finished : StepAction
    data object Blocked : StepAction
}
