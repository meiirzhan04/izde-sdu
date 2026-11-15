package com.sdu.izdesdu.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sdu.izdesdu.R
import com.sdu.izdesdu.di.AppContainer
import com.sdu.izdesdu.domain.model.CheckPoint
import com.sdu.izdesdu.domain.model.Route
import com.sdu.izdesdu.presentation.theme.IzdeBlue
import com.sdu.izdesdu.presentation.theme.IzdeBlueHalo
import com.sdu.izdesdu.presentation.theme.IzdeBorderMuted
import com.sdu.izdesdu.presentation.theme.IzdeDragHandle
import com.sdu.izdesdu.presentation.theme.IzdeGreen
import com.sdu.izdesdu.presentation.theme.IzdeMapBg
import com.sdu.izdesdu.presentation.theme.IzdeMintBg
import com.sdu.izdesdu.presentation.theme.IzdeOrange
import com.sdu.izdesdu.presentation.theme.IzdeOutline
import com.sdu.izdesdu.presentation.theme.IzdeTextMuted
import com.sdu.izdesdu.presentation.theme.IzdeTextPrimary
import com.sdu.izdesdu.presentation.theme.IzdeTextSecondary

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
            .clickable(onClick = onClick)
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
        topLineDone -> IzdeGreen
        else -> IzdeOutline
    }
    val bottomLineColor = when {
        isLast -> Color.Transparent
        isDone -> IzdeGreen
        else -> IzdeOutline
    }
    val circleColor = when {
        isDone -> IzdeGreen
        isCurrent -> IzdeBlue
        else -> Color.White
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
                .background(if (isCurrent) IzdeBlueHalo else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(circleColor)
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
                .background(bottomLineColor)
        )
    }
}
