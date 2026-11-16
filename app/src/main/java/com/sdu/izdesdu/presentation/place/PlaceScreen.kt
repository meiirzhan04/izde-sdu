package com.sdu.izdesdu.presentation.place

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sdu.izdesdu.R
import com.sdu.izdesdu.di.AppContainer
import com.sdu.izdesdu.domain.model.Place
import com.sdu.izdesdu.presentation.components.CampusMap
import com.sdu.izdesdu.presentation.components.PlacePin
import com.sdu.izdesdu.presentation.components.questionsLabel
import com.sdu.izdesdu.presentation.theme.IzdeBlue
import com.sdu.izdesdu.presentation.theme.IzdeMapBg
import com.sdu.izdesdu.presentation.theme.IzdeSurfaceCard
import com.sdu.izdesdu.presentation.theme.IzdeTextBody
import com.sdu.izdesdu.presentation.theme.IzdeTextPrimary
import com.sdu.izdesdu.presentation.theme.IzdeTextSecondary

@Composable
fun PlaceScreen(
    placeId: String,
    appContainer: AppContainer,
    onBack: () -> Unit,
    onStartRoute: (String) -> Unit,
    viewModel: PlaceViewModel = viewModel(
        key = "place_$placeId",
        factory = PlaceViewModel.factory(placeId, appContainer)
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val place = uiState.place

    if (place != null) {
        PlaceContent(
            place = place,
            onBack = onBack,
            onStartRoute = onStartRoute
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.place_not_found),
                    color = IzdeTextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(onClick = onBack) {
                    Text(text = stringResource(R.string.route_back))
                }
            }
        }
    }
}

@Composable
fun PlaceContent(
    place: Place,
    onBack: () -> Unit,
    onStartRoute: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(IzdeMapBg)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            CampusMap(
                places = listOf(place),
                selectedId = place.id,
                onPinClick = {},
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.route_back),
                    tint = IzdeTextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PlacePin(color = Color(place.colorHex), size = 28.dp, borderWidth = 0.dp)
                Text(
                    text = place.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = IzdeTextPrimary
                )
            }

            Text(
                text = place.block,
                style = MaterialTheme.typography.labelLarge,
                color = IzdeTextBody,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(IzdeSurfaceCard)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )

            Text(
                text = place.description,
                style = MaterialTheme.typography.bodyMedium,
                color = IzdeTextBody
            )

            Text(
                text = stringResource(R.string.place_nearby_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = IzdeTextPrimary
            )
            place.nearby.forEach { item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(IzdeBlue)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyMedium,
                        color = IzdeTextBody
                    )
                }
            }

            Text(
                text = stringResource(
                    R.string.place_on_the_way,
                    questionsLabel(place.questionsCount)
                ),
                style = MaterialTheme.typography.bodySmall,
                color = IzdeTextSecondary
            )

            Text(
                text = stringResource(R.string.place_start_route_here),
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(place.colorHex))
                    .clickable { onStartRoute(place.id) }
                    .padding(vertical = 16.dp)
            )
        }
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
fun PlaceContentPreview() {
    PlaceContent(
        place = Place(
            id = "canteen",
            title = "Столовая",
            block = "Блок F",
            description = "Здесь обедают студенты. Рядом кофейня, а за ней зона со столиками.",
            nearby = listOf("Кофейня", "Зона со столиками", "Аудитории блока F"),
            questionsCount = 5,
            colorHex = 0xFFE0791B,
            mapX = 163.2f,
            mapY = 462.8f
        ),
        onBack = {},
        onStartRoute = {}
    )
}
