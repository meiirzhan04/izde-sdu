package com.sdu.izdesdu.presentation.place

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sdu.izdesdu.data.repository.PlaceRepositoryImpl
import com.sdu.izdesdu.domain.model.Place
import com.sdu.izdesdu.presentation.components.CampusMap
import com.sdu.izdesdu.presentation.components.IzdeColors
import com.sdu.izdesdu.presentation.components.PlacePin
import com.sdu.izdesdu.presentation.components.questionsLabel

@Composable
fun PlaceScreen(
    placeId: String,
    onBack: () -> Unit,
    onStartRoute: (String) -> Unit,
    viewModel: PlaceViewModel = viewModel()
) {
    val place = viewModel.getPlace(placeId)

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
            Text(text = "Место не найдено", color = IzdeColors.TextSecondary)
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
            .background(IzdeColors.MapBackground)
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
                Text(
                    text = "←",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = IzdeColors.Dark
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
                PlacePin(color = place.color, size = 28.dp, borderWidth = 0.dp)
                Text(
                    text = place.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = IzdeColors.Dark
                )
            }

            Text(
                text = place.block,
                style = MaterialTheme.typography.labelLarge,
                color = IzdeColors.TextBody,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(IzdeColors.Chip)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )

            Text(
                text = place.description,
                style = MaterialTheme.typography.bodyMedium,
                color = IzdeColors.TextBody
            )

            Text(
                text = "Что рядом",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = IzdeColors.Dark
            )
            place.nearby.forEach { item ->
                Text(
                    text = "• $item",
                    style = MaterialTheme.typography.bodyMedium,
                    color = IzdeColors.TextBody
                )
            }

            Text(
                text = "По пути: ${questionsLabel(place.questionsCount)}",
                style = MaterialTheme.typography.bodySmall,
                color = IzdeColors.TextSecondary
            )

            Text(
                text = "Начать путь сюда",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(place.color)
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
        place = PlaceRepositoryImpl().getPlaces()[2],
        onBack = {},
        onStartRoute = {}
    )
}