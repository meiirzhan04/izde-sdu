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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sdu.izdesdu.R
import com.sdu.izdesdu.di.AppContainer
import com.sdu.izdesdu.domain.model.Place
import com.sdu.izdesdu.presentation.components.CampusMap
import com.sdu.izdesdu.presentation.components.PlaceCard
import com.sdu.izdesdu.presentation.theme.IzdeBlue
import com.sdu.izdesdu.presentation.theme.IzdeDragHandle
import com.sdu.izdesdu.presentation.theme.IzdeMapBg
import com.sdu.izdesdu.presentation.theme.IzdeOutline
import com.sdu.izdesdu.presentation.theme.IzdeTextPrimary
import com.sdu.izdesdu.presentation.theme.IzdeTextSecondary

@Composable
fun MapScreen(
    appContainer: AppContainer,
    onStartRoute: (String) -> Unit,
    onOpenPlace: (String) -> Unit,
    viewModel: MapViewModel = viewModel(
        factory = MapViewModel.factory(appContainer)
    )
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
            uiState = uiState,
            onSelectPlace = onSelectPlace,
            onStartRoute = onStartRoute,
            onOpenPlace = onOpenPlace
        )
    }
}

@Composable
fun MapHeader(
    totalScore: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.map_brand_izde),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = IzdeTextPrimary,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Text(
                text = stringResource(R.string.map_brand_sdu),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(IzdeBlue)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = IzdeTextPrimary,
            shadowElevation = 4.dp
        ) {
            Text(
                text = stringResource(R.string.question_score_pill, totalScore),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
            )
        }
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
                .background(IzdeDragHandle)
        )

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = stringResource(R.string.map_where_to_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = IzdeTextPrimary
            )
            Text(
                text = stringResource(R.string.map_you_are_at_gate),
                style = MaterialTheme.typography.bodyMedium,
                color = IzdeTextSecondary
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
                    text = stringResource(R.string.map_about_place),
                    color = IzdeTextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clip(buttonShape)
                        .border(2.dp, IzdeOutline, buttonShape)
                        .clickable { onOpenPlace(selected.id) }
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                )
                Text(
                    text = stringResource(R.string.map_start_route),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(buttonShape)
                        .background(Color(selected.colorHex))
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
    MapContent(
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
