package com.sdu.izdesdu.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sdu.izdesdu.R
import com.sdu.izdesdu.domain.model.Place
import com.sdu.izdesdu.presentation.theme.IzdeOutline
import com.sdu.izdesdu.presentation.theme.IzdeTextPrimary
import com.sdu.izdesdu.presentation.theme.IzdeTextSecondary

@Composable
fun PlacesSheet(
    places: List<Place>,
    selected: Place?,
    onSelectPlace: (String) -> Unit,
    onStartRoute: (String) -> Unit,
    onOpenPlace: (String) -> Unit
) {
    val buttonShape = RoundedCornerShape(16.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(Color.White)
            .padding(top = 10.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SheetDragHandle(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            width = 44.dp,
            height = 5.dp
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
            items(places) { place ->
                PlaceCard(
                    place = place,
                    selected = place.id == selected?.id,
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
