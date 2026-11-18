package com.sdu.izdesdu.presentation.question

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun QuestionScreen(
    routeId: String,
    step: Int,
    onBack: () -> Unit,
    onNextStep: (Int) -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<QuestionViewModel>(key = "question_${routeId}_$step") {
        parametersOf(routeId, step)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    QuestionScreenContent(
        uiState = uiState,
        onBack = onBack,
        onFinish = onFinish,
        onSelectAnswer = viewModel::selectAnswer,
        onShowHint = viewModel::showHint,
        onContinue = {
            when (val action = viewModel.finishCurrentStep()) {
                is StepAction.Next -> onNextStep(action.step)
                StepAction.Finished, StepAction.Blocked -> Unit
            }
        },
        modifier = modifier
    )
}
