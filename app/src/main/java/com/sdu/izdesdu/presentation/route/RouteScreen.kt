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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import kotlin.math.roundToInt

private val PrimaryBlue = Color(0xFF2F66D8)
private val SuccessGreen = Color(0xFF2E9E6B)
private val SuccessMintBg = Color(0xFFE7F5EE)
private val AccentOrange = Color(0xFFE0791B)
private val TextPrimary = Color(0xFF1E2A3A)
private val TextSecondary = Color(0xFF55606E)
private val TextMuted = Color(0xFF8A94A0)
private val MapBackground = Color(0xFFE4EDE3)
private val CurrentRowBg = Color(0xFFEEF4FF)
private val RowBg = Color(0xFFF8FAFC)
private val UpcomingBadgeBg = Color(0xFFE9EEF4)
private val OutlineColor = Color(0xFFE3E8EE)
private val DragHandleColor = Color(0xFFDDE3EA)

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
                Text(text = stringResource(R.string.route_not_found), color = TextPrimary)
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
            .background(MapBackground)
    ) {
        RouteMapPreview(
            checkpoints = route.checkpoints,
            currentStep = uiState.currentStep,
            isCompleted = uiState.isCompleted,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.32f)
                .align(Alignment.TopCenter)
        )

        FloatingRouteTopBar(
            routeTitle = route.title,
            onBack = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.68f)
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
                        .background(DragHandleColor)
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
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = SuccessMintBg,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.route_points_per_step),
                            color = SuccessGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (uiState.isCompleted) {
                        stringResource(R.string.route_completed_badge)
                    } else {
                        stringResource(R.string.route_subtitle)
                    },
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = if (uiState.isCompleted) SuccessGreen else TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(route.checkpoints, key = { _, cp -> cp.id }) { index, checkpoint ->
                        val step = index + 1
                        val isDone = uiState.isCompleted || step < uiState.currentStep
                        val isCurrent = !uiState.isCompleted && step == uiState.currentStep
                        CheckpointItem(
                            step = step,
                            checkpoint = checkpoint,
                            isDone = isDone,
                            isCurrent = isCurrent,
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
                            .weight(0.36f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, OutlineColor)
                    ) {
                        Text(
                            text = stringResource(R.string.route_cancel),
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Button(
                        onClick = { onStartStep(uiState.currentStep) },
                        modifier = Modifier
                            .weight(0.64f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
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
            val nx = 0.22f + 0.56f * ((cp.mapX - minX) / spanX)
            val ny = 0.36f + 0.52f * ((cp.mapY - minY) / spanY)
            Offset(widthPx * nx, heightPx * ny)
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val dash = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
            for (i in 0 until points.lastIndex) {
                drawLine(
                    color = AccentOrange,
                    start = points[i],
                    end = points[i + 1],
                    strokeWidth = 5f,
                    pathEffect = dash
                )
            }
        }

        checkpoints.forEachIndexed { index, _ ->
            val step = index + 1
            val isDone = isCompleted || step < currentStep
            val isCurrent = !isCompleted && step == currentStep
            val center = points[index]
            val pinBg = when {
                isDone -> SuccessGreen
                isCurrent -> PrimaryBlue
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
                    .background(pinBg)
                    .border(
                        width = 2.dp,
                        color = if (isDone || isCurrent) Color.White else AccentOrange,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isDone) "✓" else step.toString(),
                    color = if (isDone || isCurrent) Color.White else TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun FloatingRouteTopBar(
    routeTitle: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val parts = routeTitle.split("→").map { it.trim() }

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
                Text(
                    text = "←",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
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
                if (parts.size == 2) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = parts[0],
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = " → ",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(AccentOrange)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = parts[1],
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Text(
                        text = routeTitle,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun CheckpointItem(
    step: Int,
    checkpoint: CheckPoint,
    isDone: Boolean,
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    val badgeBg = when {
        isDone -> SuccessGreen
        isCurrent -> PrimaryBlue
        else -> UpcomingBadgeBg
    }
    val statusText = when {
        isDone -> stringResource(R.string.route_step_done)
        isCurrent -> stringResource(R.string.route_step_current)
        else -> stringResource(R.string.route_step_upcoming)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = if (isCurrent) CurrentRowBg else RowBg,
        border = BorderStroke(1.dp, if (isCurrent) PrimaryBlue else OutlineColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(badgeBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isDone) "✓" else step.toString(),
                    color = if (isDone || isCurrent) Color.White else TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "$step. ${checkpoint.title}",
                fontSize = 14.sp,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.SemiBold,
                color = TextPrimary,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = statusText,
                fontSize = 12.sp,
                color = when {
                    isDone -> SuccessGreen
                    isCurrent -> PrimaryBlue
                    else -> TextMuted
                },
                fontWeight = if (isCurrent) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}
