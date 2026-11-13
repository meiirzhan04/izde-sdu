package com.sdu.izdesdu.presentation.map

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sdu.izdesdu.data.repository.PlaceRepositoryImpl
import com.sdu.izdesdu.presentation.components.CampusMap
import com.sdu.izdesdu.presentation.components.IzdeColors
import com.sdu.izdesdu.presentation.components.PlaceCard

@Composable
fun MapScreen(
    onStartRoute: (String) -> Unit,
    onOpenPlace: (String) -> Unit,
    viewModel: MapViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    MapContent(
        uiState = uiState,
        onSelectPlace = { id -> viewModel.selectPlace(id) },
        onStartRoute = onStartRoute,
        onOpenPlace = onOpenPlace
    )
}

@Composable
fun MapContent(
    uiState: MapUiState,
    onSelectPlace: (String) -> Unit,
    onStartRoute: (String) -> Unit,
    onOpenPlace: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(IzdeColors.MapBackground)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            CampusMap(
                places = uiState.places,
                selectedId = uiState.selectedId,
                onPinClick = onSelectPlace,
                modifier = Modifier.fillMaxSize()
            )
            MapHeader(modifier = Modifier.padding(16.dp))
        }

        PlacesSheet(
            uiState = uiState,
            onSelectPlace = onSelectPlace,
            onStartRoute = onStartRoute,
            onOpenPlace = onOpenPlace
        )
    }
}

@Composable
fun MapHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Ізде",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = IzdeColors.Dark,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Text(
            text = "SDU",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(IzdeColors.Primary)
                .padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
fun PlacesSheet(
    uiState: MapUiState,
    onSelectPlace: (String) -> Unit,
    onStartRoute: (String) -> Unit,
    onOpenPlace: (String) -> Unit
) {
    val selected = uiState.selectedPlace
    val buttonShape = RoundedCornerShape(16.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(Color.White)
            .padding(top = 10.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(width = 44.dp, height = 5.dp)
                .clip(RoundedCornerShape(50))
                .background(IzdeColors.Handle)
        )

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "Куда идём?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = IzdeColors.Dark
            )
            Text(
                text = "Ты у главного входа",
                style = MaterialTheme.typography.bodyMedium,
                color = IzdeColors.TextSecondary
            )
        }

        LazyRow(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.places) { place ->
                PlaceCard(
                    place = place,
                    selected = place.id == uiState.selectedId,
                    onClick = { onSelectPlace(place.id) }
                )
            }
        }

        if (selected != null) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "О месте",
                    color = IzdeColors.Dark,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clip(buttonShape)
                        .border(2.dp, IzdeColors.Outline, buttonShape)
                        .clickable { onOpenPlace(selected.id) }
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                )
                Text(
                    text = "Начать путь",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(buttonShape)
                        .background(selected.color)
                        .clickable { onStartRoute(selected.id) }
                        .padding(vertical = 16.dp)
                )
            }
        }
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
fun MapContentPreview() {
    MapContent(
        uiState = MapUiState(
            places = PlaceRepositoryImpl().getPlaces(),
            selectedId = "canteen"
        ),
        onSelectPlace = {},
        onStartRoute = {},
        onOpenPlace = {}
    )
}