package com.sdu.izdesdu.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun WeatherRemoteIcon(
    iconUrl: String,
    contentDescription: String?,
    placeholderColor: Color,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageRequest = remember(context, iconUrl) {
        ImageRequest.Builder(context)
            .data(iconUrl)
            .crossfade(true)
            .build()
    }

    SubcomposeAsyncImage(
        model = imageRequest,
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        loading = { WeatherIconPlaceholder(placeholderColor) },
        error = { WeatherIconPlaceholder(placeholderColor) },
        modifier = modifier
    )
}

@Composable
private fun WeatherIconPlaceholder(color: Color) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
            .clip(CircleShape)
            .background(color)
    )
}
