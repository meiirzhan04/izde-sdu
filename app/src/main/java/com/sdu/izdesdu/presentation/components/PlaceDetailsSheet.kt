package com.sdu.izdesdu.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import com.sdu.izdesdu.presentation.theme.IzdeBlue
import com.sdu.izdesdu.presentation.theme.IzdeSurfaceCard
import com.sdu.izdesdu.presentation.theme.IzdeTextBody
import com.sdu.izdesdu.presentation.theme.IzdeTextPrimary
import com.sdu.izdesdu.presentation.theme.IzdeTextSecondary

@Composable
fun PlaceDetailsSheet(
    place: Place,
    onStartRoute: (String) -> Unit
) {
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
