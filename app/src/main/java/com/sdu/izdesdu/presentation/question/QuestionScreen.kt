package com.sdu.izdesdu.presentation.question

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sdu.izdesdu.presentation.components.AnswerOption
import com.sdu.izdesdu.R
import com.sdu.izdesdu.di.AppContainer
import com.sdu.izdesdu.domain.model.CheckPoint
import com.sdu.izdesdu.domain.model.Question
import com.sdu.izdesdu.presentation.route.RouteMapPreview
import com.sdu.izdesdu.presentation.theme.IzdeBlue
import com.sdu.izdesdu.presentation.theme.IzdeDragHandle
import com.sdu.izdesdu.presentation.theme.IzdeError
import com.sdu.izdesdu.presentation.theme.IzdeErrorBg
import com.sdu.izdesdu.presentation.theme.IzdeGreen
import com.sdu.izdesdu.presentation.theme.IzdeHintBg
import com.sdu.izdesdu.presentation.theme.IzdeMapBg
import com.sdu.izdesdu.presentation.theme.IzdeMintBg
import com.sdu.izdesdu.presentation.theme.IzdeOutline
import com.sdu.izdesdu.presentation.theme.IzdeSurfaceCard
import com.sdu.izdesdu.presentation.theme.IzdeTextPrimary
import com.sdu.izdesdu.presentation.theme.IzdeTextSecondary

private val optionLetters = listOf("А", "Б", "В", "Г")

@Composable
fun QuestionScreen(
    routeId: String,
    step: Int,
    appContainer: AppContainer,
    onBack: () -> Unit,
    onNextStep: (Int) -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuestionViewModel = viewModel(
        key = "question_${routeId}_$step",
        factory = QuestionViewModel.factory(routeId, step, appContainer)
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val route = uiState.route
    val checkpoint = uiState.checkpoint
    val question = uiState.question

    if (uiState.isMissing || route == null || checkpoint == null || question == null) {
        QuestionNotFoundView(onBack = onBack, modifier = modifier)
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
                    totalScore = uiState.totalScore,
                    onBackToMap = onFinish
                )
            } else {
                QuestionSheetContent(
                    uiState = uiState,
                    checkpoint = checkpoint,
                    question = question,
                    onSelectAnswer = viewModel::selectAnswer,
                    onShowHint = viewModel::showHint,
                    onContinue = {
                        if (!viewModel.finishCurrentStep()) {
                            onNextStep(uiState.checkpointNumber + 1)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun QuestionNotFoundView(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.question_not_found), color = IzdeTextPrimary)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(onClick = onBack) {
                Text(text = stringResource(R.string.route_back))
            }
        }
    }
}

@Composable
private fun QuestionSheetContent(
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

        QuestionBody(
            uiState = uiState,
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
private fun SheetDragHandle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(width = 36.dp, height = 4.dp)
            .clip(CircleShape)
            .background(IzdeDragHandle)
    )
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
private fun QuestionBody(
    uiState: QuestionUiState,
    instruction: String,
    question: Question,
    onSelectAnswer: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (instruction.isNotBlank()) {
            Text(
                text = instruction,
                fontSize = 12.sp,
                color = IzdeTextSecondary
            )
        }

        Text(
            text = question.text,
            fontSize = 17.sp,
            lineHeight = 23.sp,
            fontWeight = FontWeight.Bold,
            color = IzdeTextPrimary
        )

        question.options.forEachIndexed { index, optionText ->
            val isSelected = uiState.selectedIndex == index
            AnswerOption(
                letter = optionLetters.getOrElse(index) { ('A' + index).toString() },
                text = optionText,
                isSelected = isSelected,
                isRight = isSelected && uiState.isCorrect == true,
                isWrong = isSelected && uiState.isCorrect == false,
                enabled = uiState.isCorrect != true,
                onClick = { onSelectAnswer(index) }
            )
        }

        if (uiState.isCorrect != null) {
            FeedbackBanner(isCorrect = uiState.isCorrect == true)
        }

        if (uiState.showHint && uiState.isCorrect != true) {
            HintBanner(hint = question.hint)
        }
    }
}

@Composable
private fun HintBanner(hint: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = IzdeHintBg,
        border = BorderStroke(1.dp, IzdeOutline)
    ) {
        Text(
            text = stringResource(R.string.question_hint_prefix, hint),
            fontSize = 13.sp,
            lineHeight = 18.sp,
            color = IzdeTextPrimary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        )
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
        enabled = !showHint,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, IzdeOutline)
    ) {
        Text(
            text = stringResource(R.string.question_hint_button),
            color = if (showHint) IzdeTextSecondary else IzdeTextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun QuestionTopBar(
    totalScore: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable(onClick = onBack),
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.question_close),
                    tint = IzdeTextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = IzdeTextPrimary,
            shadowElevation = 4.dp
        ) {
            Text(
                text = stringResource(R.string.question_score_pill, totalScore),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
            )
        }
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

@Composable
private fun FeedbackBanner(isCorrect: Boolean) {
    val bgColor = if (isCorrect) IzdeMintBg else IzdeErrorBg
    val fgColor = if (isCorrect) IzdeGreen else IzdeError

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = bgColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isCorrect) Icons.Filled.Check else Icons.Filled.Close,
                contentDescription = null,
                tint = fgColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(
                    if (isCorrect) R.string.question_correct_feedback else R.string.question_wrong_feedback
                ),
                color = fgColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun RouteFinishSheet(
    routeTitle: String,
    destinationTitle: String,
    totalCheckpoints: Int,
    totalScore: Int,
    onBackToMap: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SheetDragHandle()

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(IzdeMintBg),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(IzdeGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.route_finish_title),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = IzdeTextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.route_finish_destination, destinationTitle),
                fontSize = 14.sp,
                color = IzdeTextSecondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = routeTitle,
                fontSize = 13.sp,
                color = IzdeTextSecondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = IzdeSurfaceCard,
                border = BorderStroke(1.dp, IzdeOutline)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FinishStatItem(
                        label = stringResource(R.string.route_finish_checkpoints_label),
                        value = stringResource(
                            R.string.route_finish_checkpoints_value,
                            totalCheckpoints,
                            totalCheckpoints
                        )
                    )
                    FinishStatItem(
                        label = stringResource(R.string.route_finish_total_score_label),
                        value = totalScore.toString()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onBackToMap,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = IzdeBlue)
        ) {
            Text(
                text = stringResource(R.string.route_back_to_map),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun FinishStatItem(
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = IzdeTextPrimary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = IzdeTextSecondary
        )
    }
}
