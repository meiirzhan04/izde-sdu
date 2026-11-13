package com.sdu.izdesdu.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sdu.izdesdu.R
import com.sdu.izdesdu.data.repository.PlaceRepositoryImpl
import com.sdu.izdesdu.domain.model.Place

@Composable
fun CampusMap(
    places: List<Place>,
    selectedId: String?,
    onPinClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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

            MapLabel("I", 40.6f, 236.2f, Color(0xFFA8642A))
            MapLabel("H", 40.6f, 315.0f, Color(0xFF3E6FA8))
            MapLabel("G", 40.6f, 389.8f, Color(0xFF62783E))
            MapLabel("F", 40.6f, 466.0f, Color(0xFF343A70))
            MapLabel("E", 40.6f, 543.4f, Color(0xFF6E1C20))
            MapLabel("D", 40.6f, 620.2f, Color(0xFF7C3283))
            MapLabel("A", 31.0f, 715.0f, Color.White)
            MapLabel("B", 105.6f, 740.0f, Color.White)
            MapLabel("C", 195.2f, 721.0f, Color(0xFF3D6B5A))
            MapLabel("SDU Life", 309.8f, 157.0f, Color(0xFFA0661A), width = 90f)
            MapLabel("Общежитие", 317.8f, 686.0f, Color(0xFF3D7A2E), width = 90f)

            YouAreHere(modifier = Modifier.offset(x = 96.dp, y = 845.dp))

            places.forEach { place ->
                val isSelected = place.id == selectedId
                val pinSize = if (isSelected) 40.dp else 28.dp

                PlacePin(
                    color = place.color,
                    size = pinSize,
                    modifier = Modifier
                        .offset(
                            x = place.mapX.dp - pinSize / 2,
                            y = place.mapY.dp - pinSize
                        )
                        .clickable { onPinClick(place.id) }
                )
            }

            val selected = places.find { it.id == selectedId }

            if (selected != null) {
                val labelX = if (selected.mapX > 250) selected.mapX - 120 else selected.mapX - 40

                Text(
                    text = selected.title,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    modifier = Modifier
                        .offset(x = labelX.dp, y = selected.mapY.dp - 76.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(IzdeColors.Dark)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun MapLabel(
    text: String,
    x: Float,
    y: Float,
    color: Color,
    width: Float = 20f
) {
    Box(
        modifier = Modifier
            .offset(x = (x - width / 2).dp, y = (y - 10).dp)
            .size(width = width.dp, height = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun YouAreHere(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(Color.White)
            .padding(3.dp)
            .clip(CircleShape)
            .background(IzdeColors.Primary)
    )
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
fun CampusMapPreview() {
    CampusMap(
        places = PlaceRepositoryImpl().getPlaces(),
        selectedId = "canteen",
        onPinClick = {},
        modifier = Modifier.fillMaxSize()
    )
}