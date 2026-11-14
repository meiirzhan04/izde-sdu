package com.sdu.izdesdu.presentation.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sdu.izdesdu.di.AppContainer
import com.sdu.izdesdu.domain.usecase.CheckAnswerUseCase
import com.sdu.izdesdu.domain.usecase.CompleteRouteUseCase
import com.sdu.izdesdu.domain.usecase.GetRouteByIdUseCase
import com.sdu.izdesdu.domain.usecase.GetUserProgressUseCase
import com.sdu.izdesdu.domain.usecase.SaveAnswerProgressUseCase
import com.sdu.izdesdu.domain.usecase.UpdateRouteStepUseCase
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
    private val getRouteByIdUseCase: GetRouteByIdUseCase,
    private val checkAnswerUseCase: CheckAnswerUseCase,
    private val getUserProgressUseCase: GetUserProgressUseCase,
    private val saveAnswerProgressUseCase: SaveAnswerProgressUseCase,
    private val updateRouteStepUseCase: UpdateRouteStepUseCase,
    private val completeRouteUseCase: CompleteRouteUseCase,
    dispatcher: CoroutineDispatcher? = null
) : ViewModel() {

    private val _uiState: MutableStateFlow<QuestionUiState>

    init {
        val route = getRouteByIdUseCase(routeId)
        val checkpoints = route?.checkpoints.orEmpty()
        val checkpoint = checkpoints.getOrNull(step - 1)
        val initialScore = getUserProgressUseCase().value.totalScore

        _uiState = MutableStateFlow(
            QuestionUiState(
                route = route,
                checkpoint = checkpoint,
                checkpointNumber = step,
                totalCheckpoints = checkpoints.size,
                totalScore = initialScore
            )
        )

        viewModelScope.launch(dispatcher ?: EmptyCoroutineContext) {
            getUserProgressUseCase().collect { progress ->
                _uiState.update { it.copy(totalScore = progress.totalScore) }
            }
        }
    }

    val uiState: StateFlow<QuestionUiState> = _uiState.asStateFlow()

    fun selectAnswer(index: Int) {
        val current = _uiState.value
        val question = current.question ?: return
        if (current.isCorrect == true || current.isRouteFinished) return
        if (index !in question.options.indices) return

        val result = checkAnswerUseCase(question, index)
        saveAnswerProgressUseCase(result.isCorrect, result.earnedPoints)

        if (result.isCorrect && !current.isLastStep) {
            updateRouteStepUseCase(routeId, step + 1)
        }

        _uiState.update { state ->
            state.copy(
                selectedIndex = index,
                isCorrect = result.isCorrect,
                showHint = state.showHint || !result.isCorrect,
                sessionEarnedScore = state.sessionEarnedScore + result.earnedPoints
            )
        }
    }

    fun showHint() {
        if (_uiState.value.question == null) return
        _uiState.update { it.copy(showHint = true) }
    }

    /** Advances to the next checkpoint or marks the route completed on the final step. */
    fun finishCurrentStep(): Boolean {
        val current = _uiState.value
        if (current.isMissing) return false

        return if (current.isLastStep) {
            completeRouteUseCase(routeId)
            _uiState.update { it.copy(isRouteFinished = true) }
            true
        } else {
            updateRouteStepUseCase(routeId, step + 1)
            false
        }
    }

    companion object {
        fun factory(
            routeId: String,
            step: Int,
            container: AppContainer
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return QuestionViewModel(
                    routeId = routeId,
                    step = step,
                    getRouteByIdUseCase = container.getRouteByIdUseCase,
                    checkAnswerUseCase = container.checkAnswerUseCase,
                    getUserProgressUseCase = container.getUserProgressUseCase,
                    saveAnswerProgressUseCase = container.saveAnswerProgressUseCase,
                    updateRouteStepUseCase = container.updateRouteStepUseCase,
                    completeRouteUseCase = container.completeRouteUseCase
                ) as T
            }
        }
    }
}
