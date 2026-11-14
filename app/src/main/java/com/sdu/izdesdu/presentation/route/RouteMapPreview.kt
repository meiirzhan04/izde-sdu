package com.sdu.izdesdu.presentation.route

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

/** Renders the schematic route path and numbered checkpoint pins on top of the map area. */
@Composable
internal fun RouteMapPreview(
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
