package com.sdu.izdesdu.presentation.question

import com.sdu.izdesdu.domain.model.CheckPoint
import com.sdu.izdesdu.domain.model.Question
import com.sdu.izdesdu.domain.model.Route

data class QuestionUiState(
    val route: Route? = null,
    val checkpoint: CheckPoint? = null,
    val checkpointNumber: Int = 1,
    val totalCheckpoints: Int = 0,
    val selectedIndex: Int? = null,
    val isCorrect: Boolean? = null,
    val showHint: Boolean = false,
    val totalScore: Int = 0,
    val routeEarnedPoints: Int = 0,
    val mistakes: Int = 0,
    val isLocked: Boolean = false,
    val isRouteFinished: Boolean = false
) {
    val question: Question? = checkpoint?.question
    val isLastStep: Boolean = totalCheckpoints in 1..checkpointNumber
    val isMissing: Boolean = route == null || checkpoint == null
}
