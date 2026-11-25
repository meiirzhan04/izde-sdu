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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sdu.izdesdu.R
import com.sdu.izdesdu.domain.model.Place

@Composable
fun PlaceCard(
    place: Place,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface)
            .border(2.dp, if (selected) Color(place.colorHex) else MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PlacePin(color = Color(place.colorHex), size = 24.dp, borderWidth = 0.dp)

        Text(
            text = place.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            minLines = 2,
            maxLines = 2
        )

        Text(
            text = questionsLabel(place.questionsCount),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun questionsLabel(count: Int): String {
    val resId = when {
        count % 10 == 1 && count % 100 != 11 -> R.string.questions_count_one
        count % 10 in 2..4 && count % 100 !in 12..14 -> R.string.questions_count_few
        else -> R.string.questions_count_many
    }
    return stringResource(resId, count)
}

@Preview
@Composable
fun PlaceCardPreview() {
    val canteen = Place(
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
    val library = Place(
        id = "library",
        title = "Библиотека",
        block = "Блок B",
        description = "",
        nearby = emptyList(),
        questionsCount = 3,
        colorHex = 0xFF2F66D8,
        mapX = 105.6f,
        mapY = 741.2f
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        PlaceCard(place = canteen, selected = true, onClick = {})
        PlaceCard(place = library, selected = false, onClick = {})
    }
}
