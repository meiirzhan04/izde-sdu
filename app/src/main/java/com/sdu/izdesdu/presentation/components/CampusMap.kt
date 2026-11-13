package com.sdu.izdesdu.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sdu.izdesdu.R

@Composable
fun CampusMap(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(IzdeColors.MapBackground)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(modifier = Modifier.requiredSize(width = 390.dp, height = 940.dp)) {
            Image(
                painter = painterResource(R.drawable.campus_map),
                contentDescription = "Карта кампуса SDU",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
fun CampusMapPreview() {
    CampusMap(modifier = Modifier.fillMaxSize())
}