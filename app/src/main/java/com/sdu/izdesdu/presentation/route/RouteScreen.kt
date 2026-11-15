package com.sdu.izdesdu.presentation.route

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
import com.sdu.izdesdu.presentation.components.CheckpointRow
import com.sdu.izdesdu.R
import com.sdu.izdesdu.di.AppContainer
import com.sdu.izdesdu.domain.model.Route
import com.sdu.izdesdu.presentation.theme.IzdeBlue
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
        RouteNotFoundView(onBack = onBack, modifier = modifier)
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
            RouteSheetContent(
                route = route,
                currentStep = uiState.currentStep,
                isCompleted = uiState.isCompleted,
                onBack = onBack,
                onStartStep = onStartStep
            )
        }
    }
}

@Composable
private fun RouteNotFoundView(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.route_not_found), color = IzdeTextPrimary)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(onClick = onBack) {
                Text(text = stringResource(R.string.route_back))
            }
        }
    }
}

@Composable
private fun RouteSheetContent(
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
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(width = 36.dp, height = 4.dp)
                .clip(CircleShape)
                .background(IzdeDragHandle)
        )

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
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
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
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }
        }
    }
}
