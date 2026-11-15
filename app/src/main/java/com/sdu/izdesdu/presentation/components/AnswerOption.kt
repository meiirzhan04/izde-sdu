package com.sdu.izdesdu.presentation.components

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

@Composable
fun AnswerOption(
    letter: String,
    text: String,
    isSelected: Boolean,
    isRight: Boolean,
    isWrong: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        isRight -> IzdeGreen
        isWrong -> IzdeError
        else -> IzdeOutline
    }
    val containerColor = when {
        isRight -> IzdeMintBg
        isWrong -> IzdeErrorBg
        else -> Color.White
    }
    val badgeBg = when {
        isRight -> IzdeGreen
        isWrong -> IzdeError
        else -> IzdeSurfaceCard
    }
    val badgeFg = if (isRight || isWrong) Color.White else IzdeTextSecondary

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = containerColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(badgeBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter,
                    color = badgeFg,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = IzdeTextPrimary
            )
            if (isRight || isWrong) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = if (isRight) Icons.Filled.Check else Icons.Filled.Close,
                    contentDescription = null,
                    tint = if (isRight) IzdeGreen else IzdeError,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

