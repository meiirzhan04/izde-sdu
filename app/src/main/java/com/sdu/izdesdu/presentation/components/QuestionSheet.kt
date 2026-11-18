package com.sdu.izdesdu.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sdu.izdesdu.R
import com.sdu.izdesdu.domain.model.CheckPoint
import com.sdu.izdesdu.domain.model.Question
import com.sdu.izdesdu.presentation.question.QuestionUiState
import com.sdu.izdesdu.presentation.theme.IzdeBlue
import com.sdu.izdesdu.presentation.theme.IzdeError
import com.sdu.izdesdu.presentation.theme.IzdeGreen
import com.sdu.izdesdu.presentation.theme.IzdeOutline
import com.sdu.izdesdu.presentation.theme.IzdeTextPrimary
import com.sdu.izdesdu.presentation.theme.IzdeTextSecondary

@Composable
fun QuestionSheet(
    uiState: QuestionUiState,
    checkpoint: CheckPoint,
    question: Question,
    onSelectAnswer: (Int) -> Unit,
    onShowHint: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp, bottom = 16.dp)
    ) {
        SheetDragHandle(modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(14.dp))

        QuestionHeader(
            stepNumber = uiState.checkpointNumber,
            totalSteps = uiState.totalCheckpoints,
            checkpointTitle = checkpoint.title
        )

        Spacer(modifier = Modifier.height(12.dp))

        StepProgressSegments(
            totalCheckpoints = uiState.totalCheckpoints,
            currentStep = uiState.checkpointNumber,
            isCurrentCorrect = uiState.isCorrect
        )

        Spacer(modifier = Modifier.height(14.dp))

        QuestionCard(
            selectedIndex = uiState.selectedIndex,
            isCorrect = uiState.isCorrect,
            showHint = uiState.showHint,
            instruction = checkpoint.instruction,
            question = question,
            onSelectAnswer = onSelectAnswer,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuestionActionButton(
            isCorrect = uiState.isCorrect == true,
            isLastStep = uiState.isLastStep,
            nextStepNumber = uiState.checkpointNumber + 1,
            showHint = uiState.showHint,
            onShowHint = onShowHint,
            onContinue = onContinue
        )
    }
}

@Composable
private fun QuestionHeader(
    stepNumber: Int,
    totalSteps: Int,
    checkpointTitle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(IzdeBlue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber.toString(),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(
                    R.string.question_step_eyebrow,
                    stepNumber,
                    totalSteps,
                    checkpointTitle
                ),
                fontSize = 12.sp,
                color = IzdeTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(R.string.question_title),
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = IzdeTextPrimary
            )
        }
    }
}

@Composable
private fun QuestionActionButton(
    isCorrect: Boolean,
    isLastStep: Boolean,
    nextStepNumber: Int,
    showHint: Boolean,
    onShowHint: () -> Unit,
    onContinue: () -> Unit
) {
    if (isCorrect) {
        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = IzdeBlue)
        ) {
            Text(
                text = if (isLastStep) {
                    stringResource(R.string.question_finish_route)
                } else {
                    stringResource(R.string.question_next_step, nextStepNumber)
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        return
    }

    OutlinedButton(
        onClick = onShowHint,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, IzdeOutline)
    ) {
        Text(
            text = stringResource(if (showHint) R.string.question_hide_hint else R.string.question_hint_button),
            color = if (showHint) IzdeTextSecondary else IzdeTextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun StepProgressSegments(
    totalCheckpoints: Int,
    currentStep: Int,
    isCurrentCorrect: Boolean?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        for (index in 0 until totalCheckpoints) {
            val barStep = index + 1
            val color = when {
                barStep < currentStep -> IzdeGreen
                barStep == currentStep && isCurrentCorrect == true -> IzdeGreen
                barStep == currentStep && isCurrentCorrect == false -> IzdeError
                barStep == currentStep -> IzdeBlue
                else -> IzdeOutline
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}
