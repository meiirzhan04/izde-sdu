package com.sdu.izdesdu.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sdu.izdesdu.R
import com.sdu.izdesdu.domain.model.Route
import com.sdu.izdesdu.presentation.theme.IzdeBlue
import com.sdu.izdesdu.presentation.theme.IzdeGreen
import com.sdu.izdesdu.presentation.theme.IzdeMintBg
import com.sdu.izdesdu.presentation.theme.IzdeOutline
import com.sdu.izdesdu.presentation.theme.IzdeTextPrimary
import com.sdu.izdesdu.presentation.theme.IzdeTextSecondary

@Composable
fun RouteSheet(
    route: Route,
    currentStep: Int,
    isCompleted: Boolean,
    onBack: () -> Unit,
    onStartStep: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp, bottom = 16.dp)
    ) {
        SheetDragHandle(modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(14.dp))

        RouteSheetHeader(routeTitle = route.title, isCompleted = isCompleted)

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(route.checkpoints, key = { _, cp -> cp.id }) { index, checkpoint ->
                val step = index + 1
                CheckpointRow(
                    step = step,
                    checkpoint = checkpoint,
                    isDone = isCompleted || step < currentStep,
                    isCurrent = !isCompleted && step == currentStep,
                    isFirst = index == 0,
                    isLast = index == route.checkpoints.lastIndex,
                    topLineDone = isCompleted || (step - 1) < currentStep,
                    onClick = { onStartStep(step) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        RouteBottomButtons(
            hasCheckpoints = route.checkpoints.isNotEmpty(),
            currentStep = currentStep,
            isCompleted = isCompleted,
            onBack = onBack,
            onStartStep = onStartStep
        )
    }
}

@Composable
private fun RouteSheetHeader(
    routeTitle: String,
    isCompleted: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = routeTitle,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = IzdeTextPrimary,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(8.dp))
        Surface(color = IzdeMintBg, shape = RoundedCornerShape(12.dp)) {
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
        val subtitleRes = if (isCompleted) R.string.route_completed_badge else R.string.route_subtitle
        Text(
            text = stringResource(subtitleRes),
            fontSize = 13.sp,
            lineHeight = 18.sp,
            color = if (isCompleted) IzdeGreen else IzdeTextSecondary
        )
        if (isCompleted) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = IzdeGreen,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun RouteBottomButtons(
    hasCheckpoints: Boolean,
    currentStep: Int,
    isCompleted: Boolean,
    onBack: () -> Unit,
    onStartStep: (Int) -> Unit
) {
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
            onClick = { onStartStep(currentStep) },
            enabled = hasCheckpoints,
            modifier = Modifier
                .weight(0.65f)
                .height(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = IzdeBlue)
        ) {
            Text(
                text = if (isCompleted) {
                    stringResource(R.string.route_view_summary)
                } else {
                    stringResource(R.string.route_start_step, currentStep)
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
