package com.sdu.izdesdu.presentation.route

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sdu.izdesdu.R
import com.sdu.izdesdu.di.AppContainer
import com.sdu.izdesdu.domain.model.CheckPoint
import com.sdu.izdesdu.presentation.theme.IzdeBlue
import com.sdu.izdesdu.presentation.theme.IzdeBlueHalo
import com.sdu.izdesdu.presentation.theme.IzdeBorderMuted
import com.sdu.izdesdu.presentation.theme.IzdeDragHandle
import com.sdu.izdesdu.presentation.theme.IzdeGreen
import com.sdu.izdesdu.presentation.theme.IzdeMapBg
import com.sdu.izdesdu.presentation.theme.IzdeMintBg
import com.sdu.izdesdu.presentation.theme.IzdeOrange
import com.sdu.izdesdu.presentation.theme.IzdeOutline
import com.sdu.izdesdu.presentation.theme.IzdeStartRed
import com.sdu.izdesdu.presentation.theme.IzdeTextMuted
import com.sdu.izdesdu.presentation.theme.IzdeTextPrimary
import com.sdu.izdesdu.presentation.theme.IzdeTextSecondary
import kotlin.math.roundToInt

@Composable
fun RouteScreen(
    routeId: String,
    appContainer: AppContainer,
    onBack: () -> Unit,
    onStartStep: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RouteViewModel = viewModel(
        key = "route_$routeId",
        factory = RouteViewModel.factory(routeId, appContainer)
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val route = uiState.route

    if (route == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(R.string.route_not_found), color = IzdeTextPrimary)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(onClick = onBack) {
                    Text(text = stringResource(R.string.route_back))
                }
            }
        }
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(IzdeMapBg)
    ) {
        RouteMapPreview(
            checkpoints = route.checkpoints,
            currentStep = uiState.currentStep,
            isCompleted = uiState.isCompleted,
            startLabel = route.fromTitle,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.46f)
                .align(Alignment.TopCenter)
        )

        FloatingRouteTopBar(
            fromTitle = route.fromTitle,
            toTitle = route.toTitle,
            onBack = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.56f)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = Color.White,
            shadowElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp, bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(width = 36.dp, height = 4.dp)
                        .clip(CircleShape)
                        .background(IzdeDragHandle)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = route.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = IzdeTextPrimary,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = IzdeMintBg,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.route_points_per_step),
                            color = IzdeGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (uiState.isCompleted) {
                            stringResource(R.string.route_completed_badge)
                        } else {
                            stringResource(R.string.route_subtitle)
                        },
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = if (uiState.isCompleted) IzdeGreen else IzdeTextSecondary
                    )
                    if (uiState.isCompleted) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = IzdeGreen,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(route.checkpoints, key = { _, cp -> cp.id }) { index, checkpoint ->
                        val step = index + 1
                        val isDone = uiState.isCompleted || step < uiState.currentStep
                        val isCurrent = !uiState.isCompleted && step == uiState.currentStep
                        val isFirst = index == 0
                        val isLast = index == route.checkpoints.lastIndex
                        val prevDone = uiState.isCompleted || (step - 1) < uiState.currentStep

                        CheckpointTimelineRow(
                            step = step,
                            checkpoint = checkpoint,
                            isDone = isDone,
                            isCurrent = isCurrent,
                            isFirst = isFirst,
                            isLast = isLast,
                            topLineDone = prevDone,
                            onClick = { onStartStep(step) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier
                            .weight(0.35f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, IzdeOutline)
                    ) {
                        Text(
                            text = stringResource(R.string.route_cancel),
                            color = IzdeTextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Button(
                        onClick = { onStartStep(uiState.currentStep) },
                        modifier = Modifier
                            .weight(0.65f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IzdeBlue)
                    ) {
                        Text(
                            text = stringResource(R.string.route_start_step, uiState.currentStep),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RouteMapPreview(
    checkpoints: List<CheckPoint>,
    currentStep: Int,
    isCompleted: Boolean,
    startLabel: String,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        if (checkpoints.isEmpty()) return@BoxWithConstraints

        val density = LocalDensity.current
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }
        val pinRadiusPx = with(density) { 14.dp.toPx() }

        val minX = checkpoints.minOf { it.mapX }
        val maxX = checkpoints.maxOf { it.mapX }
        val minY = checkpoints.minOf { it.mapY }
        val maxY = checkpoints.maxOf { it.mapY }
        val spanX = (maxX - minX).takeIf { it > 0f } ?: 1f
        val spanY = (maxY - minY).takeIf { it > 0f } ?: 1f

        val points = checkpoints.map { cp ->
            val nx = 0.28f + 0.36f * ((cp.mapX - minX) / spanX)
            val ny = 0.24f + 0.44f * ((cp.mapY - minY) / spanY)
            Offset(widthPx * nx, heightPx * ny)
        }
        val gateStart = Offset(widthPx * 0.28f, heightPx * 0.88f)

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawLine(
                color = IzdeGreen,
                start = gateStart,
                end = points.first(),
                strokeWidth = 8f,
                cap = StrokeCap.Round
            )
            val dash = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
            for (i in 0 until points.lastIndex) {
                drawLine(
                    color = IzdeOrange,
                    start = points[i],
                    end = points[i + 1],
                    strokeWidth = 6f,
                    pathEffect = dash,
                    cap = StrokeCap.Round
                )
            }
        }

        if (startLabel.isNotBlank()) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 68.dp, bottom = 18.dp),
                shape = RoundedCornerShape(10.dp),
                color = IzdeStartRed
            ) {
                Text(
                    text = startLabel,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        checkpoints.forEachIndexed { index, _ ->
            val step = index + 1
            val isDone = isCompleted || step < currentStep
            val isCurrent = !isCompleted && step == currentStep
            val center = points[index]
            val pinBg = when {
                isDone -> IzdeGreen
                isCurrent -> IzdeBlue
                else -> Color.White
            }

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (center.x - pinRadiusPx).roundToInt(),
                            (center.y - pinRadiusPx).roundToInt()
                        )
                    }
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (isCurrent) IzdeBlueHalo else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(pinBg)
                        .border(
                            width = 2.dp,
                            color = if (isDone || isCurrent) Color.White else IzdeOrange,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isDone) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    } else {
                        Text(
                            text = step.toString(),
                            color = if (isCurrent) Color.White else IzdeTextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingRouteTopBar(
    fromTitle: String,
    toTitle: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.route_back),
                    tint = IzdeTextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(IzdeBlue)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = fromTitle,
                    color = IzdeTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = IzdeTextMuted,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .size(14.dp)
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(IzdeOrange)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = toTitle,
                    color = IzdeTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun CheckpointTimelineRow(
    step: Int,
    checkpoint: CheckPoint,
    isDone: Boolean,
    isCurrent: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    topLineDone: Boolean,
    onClick: () -> Unit
) {
    val statusText = when {
        isDone -> stringResource(R.string.route_step_done)
        isCurrent -> stringResource(R.string.route_step_current)
        else -> stringResource(R.string.route_step_upcoming)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .width(30.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .weight(1f)
                    .background(
                        when {
                            isFirst -> Color.Transparent
                            topLineDone -> IzdeGreen
                            else -> IzdeOutline
                        }
                    )
            )
            Box(
                modifier = Modifier
                    .size(if (isCurrent) 28.dp else 24.dp)
                    .clip(CircleShape)
                    .background(if (isCurrent) IzdeBlueHalo else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isDone -> IzdeGreen
                                isCurrent -> IzdeBlue
                                else -> Color.White
                            }
                        )
                        .then(
                            if (!isDone && !isCurrent) {
                                Modifier.border(1.5.dp, IzdeBorderMuted, CircleShape)
                            } else {
                                Modifier
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isDone) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    } else {
                        Text(
                            text = step.toString(),
                            color = if (isCurrent) Color.White else IzdeTextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .weight(1f)
                    .background(
                        when {
                            isLast -> Color.Transparent
                            isDone -> IzdeGreen
                            else -> IzdeOutline
                        }
                    )
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = "$step. ${checkpoint.title}",
            fontSize = 14.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
            color = if (isDone) IzdeTextSecondary else IzdeTextPrimary,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.width(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = statusText,
                fontSize = 12.sp,
                color = when {
                    isDone -> IzdeGreen
                    isCurrent -> IzdeBlue
                    else -> IzdeTextMuted
                },
                fontWeight = if (isCurrent) FontWeight.Medium else FontWeight.Normal
            )
            if (isDone) {
                Spacer(modifier = Modifier.width(3.dp))
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = IzdeGreen,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}
