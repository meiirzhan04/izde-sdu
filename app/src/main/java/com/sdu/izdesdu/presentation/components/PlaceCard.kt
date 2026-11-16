package com.sdu.izdesdu.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sdu.izdesdu.data.repository.PlaceRepositoryImpl
import com.sdu.izdesdu.domain.model.Place

@Composable
fun PlaceCard(
    place: Place,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(20.dp)
    val backgroundColor = if (selected) IzdeColors.SelectedCard else Color.White
    val borderColor = if (selected) Color(place.colorHex) else IzdeColors.Outline

    Column(
        modifier = modifier
            .width(160.dp)
            .clip(shape)
            .background(backgroundColor)
            .border(2.dp, borderColor, shape)
            .clickable { onClick() }
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PlacePin(color = Color(place.colorHex), size = 24.dp, borderWidth = 0.dp)

        Text(
            text = place.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = IzdeColors.Dark,
            minLines = 2,
            maxLines = 2
        )

        Text(
            text = questionsLabel(place.questionsCount),
            style = MaterialTheme.typography.bodySmall,
            color = IzdeColors.TextSecondary
        )
    }
}

fun questionsLabel(count: Int): String {
    val word = when {
        count % 10 == 1 && count % 100 != 11 -> "вопрос"
        count % 10 in 2..4 && count % 100 !in 12..14 -> "вопроса"
        else -> "вопросов"
    }
    return "$count $word про SDU"
}

@Preview
@Composable
fun PlaceCardPreview() {
    val places = PlaceRepositoryImpl().getPlaces()
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        PlaceCard(place = places[2], selected = true, onClick = {})
        PlaceCard(place = places[4], selected = false, onClick = {})
    }
}