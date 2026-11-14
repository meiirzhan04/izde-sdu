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
    val sessionEarnedScore: Int = 0,
    val isRouteFinished: Boolean = false
) {
    val question: Question?
        get() = checkpoint?.question

    val isLastStep: Boolean
        get() = totalCheckpoints > 0 && checkpointNumber >= totalCheckpoints

    val isMissing: Boolean
        get() = route == null || checkpoint == null
}
