package com.sdu.izdesdu.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
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

@Composable
fun CheckpointRow(
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
            .clickable(enabled = isCurrent || isDone, onClick = onClick)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimelineNodeIndicator(
            step = step,
            isDone = isDone,
            isCurrent = isCurrent,
            isFirst = isFirst,
            isLast = isLast,
            topLineDone = topLineDone
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = "$step. ${checkpoint.title}",
            fontSize = 14.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
            color = if (isDone) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
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
                    isDone -> MaterialTheme.colorScheme.secondary
                    isCurrent -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                },
                fontWeight = if (isCurrent) FontWeight.Medium else FontWeight.Normal
            )
            if (isDone) {
                Spacer(modifier = Modifier.width(3.dp))
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

@Composable
private fun TimelineNodeIndicator(
    step: Int,
    isDone: Boolean,
    isCurrent: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    topLineDone: Boolean
) {
    val topLineColor = when {
        isFirst -> Color.Transparent
        topLineDone -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    val bottomLineColor = when {
        isLast -> Color.Transparent
        isDone -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    val circleColor = when {
        isDone -> MaterialTheme.colorScheme.secondary
        isCurrent -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surface
    }

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
                .background(topLineColor)
        )
        Box(
            modifier = Modifier
                .size(if (isCurrent) 28.dp else 24.dp)
                .clip(CircleShape)
                .background(if (isCurrent) MaterialTheme.colorScheme.primaryContainer else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(circleColor)
                    .then(
                        if (!isDone && !isCurrent) {
                            Modifier.border(1.5.dp, MaterialTheme.colorScheme.outline, CircleShape)
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
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(13.dp)
                    )
                } else {
                    Text(
                        text = step.toString(),
                        color = if (isCurrent) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
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
                .background(bottomLineColor)
        )
    }
}
