package com.sdu.izdesdu.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sdu.izdesdu.domain.model.CheckPoint
import com.sdu.izdesdu.presentation.theme.IzdeBlue
import com.sdu.izdesdu.presentation.theme.IzdeBlueHalo
import com.sdu.izdesdu.presentation.theme.IzdeGreen
import com.sdu.izdesdu.presentation.theme.IzdeOrange
import com.sdu.izdesdu.presentation.theme.IzdeStartRed
import com.sdu.izdesdu.presentation.theme.IzdeTextPrimary
import kotlin.math.roundToInt

@Composable
fun RouteMapPreview(
    checkpoints: List<CheckPoint>,
    currentStep: Int,
    isCompleted: Boolean,
    startLabel: String,
    modifier: Modifier = Modifier
) {
    if (checkpoints.isEmpty()) return
    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }
        val pinRadiusPx = with(density) { 14.dp.toPx() }

        val points = normalizeCheckpointOffsets(checkpoints, widthPx, heightPx)
        val gateStart = Offset(widthPx * 0.28f, heightPx * 0.88f)

        RoutePathLines(gateStart = gateStart, points = points)

        if (startLabel.isNotBlank()) {
            StartPointBadge(
                label = startLabel,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 68.dp, bottom = 18.dp)
            )
        }

        points.forEachIndexed { index, center ->
            val step = index + 1
            CheckpointMapPin(
                step = step,
                center = center,
                pinRadiusPx = pinRadiusPx,
                isDone = isCompleted || step < currentStep,
                isCurrent = !isCompleted && step == currentStep
            )
        }
    }
}

private fun normalizeCheckpointOffsets(
    checkpoints: List<CheckPoint>,
    widthPx: Float,
    heightPx: Float
): List<Offset> {
    val minX = checkpoints.minOf { it.mapX }
    val maxX = checkpoints.maxOf { it.mapX }
    val minY = checkpoints.minOf { it.mapY }
    val maxY = checkpoints.maxOf { it.mapY }
    val spanX = (maxX - minX).takeIf { it > 0f } ?: 1f
    val spanY = (maxY - minY).takeIf { it > 0f } ?: 1f

    return checkpoints.map { cp ->
        val nx = 0.28f + 0.36f * ((cp.mapX - minX) / spanX)
        val ny = 0.24f + 0.44f * ((cp.mapY - minY) / spanY)
        Offset(widthPx * nx, heightPx * ny)
    }
}

@Composable
private fun RoutePathLines(
    gateStart: Offset,
    points: List<Offset>
) {
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
}

@Composable
private fun StartPointBadge(
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = IzdeStartRed
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Composable
private fun CheckpointMapPin(
    step: Int,
    center: Offset,
    pinRadiusPx: Float,
    isDone: Boolean,
    isCurrent: Boolean
) {
    val pinBg = when {
        isDone -> IzdeGreen
        isCurrent -> IzdeBlue
        else -> Color.White
    }
    val pinBorder = if (isDone || isCurrent) Color.White else IzdeOrange

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
                .border(2.dp, pinBorder, CircleShape),
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
