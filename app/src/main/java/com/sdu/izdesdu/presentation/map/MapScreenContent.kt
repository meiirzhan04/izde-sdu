package com.sdu.izdesdu.presentation.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sdu.izdesdu.domain.model.Place
import com.sdu.izdesdu.presentation.components.CampusMap
import com.sdu.izdesdu.presentation.components.MapHeader
import com.sdu.izdesdu.presentation.components.PlacesSheet
import com.sdu.izdesdu.presentation.theme.IzdeMapBg

@Composable
fun MapScreenContent(
    uiState: MapUiState,
    onSelectPlace: (String) -> Unit,
    onStartRoute: (String) -> Unit,
    onOpenPlace: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(IzdeMapBg)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            CampusMap(
                places = uiState.places,
                selectedId = uiState.selectedId,
                onPinClick = onSelectPlace,
                modifier = Modifier.fillMaxSize()
            )
            MapHeader(
                totalScore = uiState.totalScore,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

        PlacesSheet(
            places = uiState.places,
            selected = uiState.selectedPlace,
            onSelectPlace = onSelectPlace,
            onStartRoute = onStartRoute,
            onOpenPlace = onOpenPlace
        )
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun MapScreenContentPreview() {
    val previewPlaces = listOf(
        Place(
            id = "canteen",
            title = "Столовая",
            block = "Блок F",
            description = "",
            nearby = emptyList(),
            questionsCount = 5,
            colorHex = 0xFFE0791B,
            mapX = 163.2f,
            mapY = 462.8f
        )
    )
    MapScreenContent(
        uiState = MapUiState(
            places = previewPlaces,
            selectedId = "canteen",
            totalScore = 20
        ),
        onSelectPlace = {},
        onStartRoute = {},
        onOpenPlace = {}
    )
}
