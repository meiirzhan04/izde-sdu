package com.sdu.izdesdu.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sdu.izdesdu.presentation.theme.IzdeDragHandle

@Composable
fun SheetDragHandle(
    modifier: Modifier = Modifier,
    width: Dp = 36.dp,
    height: Dp = 4.dp
) {
    Box(
        modifier = modifier
            .size(width = width, height = height)
            .clip(CircleShape)
            .background(IzdeDragHandle)
    )
}
