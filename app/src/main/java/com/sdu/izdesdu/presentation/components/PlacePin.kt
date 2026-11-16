package com.sdu.izdesdu.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sdu.izdesdu.presentation.theme.IzdeOrange

@Composable
fun PlacePin(
    color: Color,
    size: Dp,
    modifier: Modifier = Modifier,
    borderWidth: Dp = 3.dp
) {
    val shape = RoundedCornerShape(
        topStartPercent = 50,
        topEndPercent = 50,
        bottomEndPercent = 50,
        bottomStartPercent = 10
    )

    Box(
        modifier = modifier
            .size(size)
            .rotate(-45f)
            .clip(shape)
            .background(color)
            .border(borderWidth, Color.White, shape)
    )
}

@Preview
@Composable
fun PlacePinPreview() {
    PlacePin(color = IzdeOrange, size = 40.dp)
}
