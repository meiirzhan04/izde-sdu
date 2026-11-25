package com.sdu.izdesdu.presentation.route

import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sdu.izdesdu.R
import com.sdu.izdesdu.presentation.components.FloatingRouteTopBar
import com.sdu.izdesdu.presentation.components.RouteMapPreview
import com.sdu.izdesdu.presentation.components.RouteSheet
import com.sdu.izdesdu.presentation.components.ScreenMessage

@Composable
fun RouteScreenContent(
    uiState: RouteUiState,
    onBack: () -> Unit,
    onStartStep: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val route = uiState.route

    if (route == null) {
        ScreenMessage(message = R.string.route_not_found, onBack = onBack, modifier = modifier)
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 10.dp
        ) {
            RouteSheet(
                route = route,
                currentStep = uiState.currentStep,
                isCompleted = uiState.isCompleted,
                onBack = onBack,
                onStartStep = onStartStep
            )
        }
    }
}
