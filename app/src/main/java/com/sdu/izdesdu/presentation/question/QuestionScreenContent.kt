package com.sdu.izdesdu.presentation.question

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sdu.izdesdu.R
import com.sdu.izdesdu.presentation.components.QuestionSheet
import com.sdu.izdesdu.presentation.components.QuestionTopBar
import com.sdu.izdesdu.presentation.components.RouteFinishSheet
import com.sdu.izdesdu.presentation.components.RouteMapPreview
import com.sdu.izdesdu.presentation.components.ScreenMessage
import com.sdu.izdesdu.presentation.theme.IzdeMapBg

@Composable
fun QuestionScreenContent(
    uiState: QuestionUiState,
    onBack: () -> Unit,
    onFinish: () -> Unit,
    onSelectAnswer: (Int) -> Unit,
    onShowHint: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val route = uiState.route
    val checkpoint = uiState.checkpoint
    val question = uiState.question

    if (uiState.isMissing || uiState.isLocked || route == null || checkpoint == null || question == null) {
        ScreenMessage(
            message = if (uiState.isLocked) R.string.question_locked else R.string.question_not_found,
            onBack = onBack,
            modifier = modifier
        )
        return
    }

    if (uiState.isRouteFinished) {
        BackHandler(onBack = onFinish)
    }

    val closeAction = if (uiState.isRouteFinished) onFinish else onBack

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(IzdeMapBg)
    ) {
        RouteMapPreview(
            checkpoints = route.checkpoints,
            currentStep = uiState.checkpointNumber,
            isCompleted = uiState.isRouteFinished || (uiState.isCorrect == true && uiState.isLastStep),
            startLabel = route.fromTitle,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.38f)
                .align(Alignment.TopCenter)
        )

        QuestionTopBar(
            totalScore = uiState.totalScore,
            onBack = closeAction,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.66f)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = Color.White,
            shadowElevation = 10.dp
        ) {
            if (uiState.isRouteFinished) {
                RouteFinishSheet(
                    routeTitle = route.title,
                    destinationTitle = route.toTitle,
                    totalCheckpoints = uiState.totalCheckpoints,
                    earnedPoints = uiState.routeEarnedPoints,
                    mistakes = uiState.mistakes,
                    onBackToMap = onFinish
                )
            } else {
                QuestionSheet(
                    uiState = uiState,
                    checkpoint = checkpoint,
                    question = question,
                    onSelectAnswer = onSelectAnswer,
                    onShowHint = onShowHint,
                    onContinue = onContinue
                )
            }
        }
    }
}
