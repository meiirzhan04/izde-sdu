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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sdu.izdesdu.R
import com.sdu.izdesdu.domain.model.Place
import com.sdu.izdesdu.presentation.theme.IzdeBlockC
import com.sdu.izdesdu.presentation.theme.IzdeBlockD
import com.sdu.izdesdu.presentation.theme.IzdeBlockE
import com.sdu.izdesdu.presentation.theme.IzdeBlockF
import com.sdu.izdesdu.presentation.theme.IzdeBlockG
import com.sdu.izdesdu.presentation.theme.IzdeBlockH
import com.sdu.izdesdu.presentation.theme.IzdeBlockI
import com.sdu.izdesdu.presentation.theme.IzdeBlue
import com.sdu.izdesdu.presentation.theme.IzdeDormLabel
import com.sdu.izdesdu.presentation.theme.IzdeGymLabel
import com.sdu.izdesdu.presentation.theme.IzdeMapBg
import com.sdu.izdesdu.presentation.theme.IzdeTextPrimary

@Composable
fun CampusMap(
    places: List<Place>,
    selectedId: String?,
    onPinClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(IzdeMapBg)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(modifier = Modifier.requiredSize(width = 390.dp, height = 940.dp)) {
            Image(
                painter = painterResource(R.drawable.campus_map),
                contentDescription = stringResource(R.string.route_map_placeholder),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            MapLabel("I", 40.6f, 236.2f, IzdeBlockI)
            MapLabel("H", 40.6f, 315.0f, IzdeBlockH)
            MapLabel("G", 40.6f, 389.8f, IzdeBlockG)
            MapLabel("F", 40.6f, 466.0f, IzdeBlockF)
            MapLabel("E", 40.6f, 543.4f, IzdeBlockE)
            MapLabel("D", 40.6f, 620.2f, IzdeBlockD)
            MapLabel("A", 31.0f, 715.0f, Color.White)
            MapLabel("B", 105.6f, 740.0f, Color.White)
            MapLabel("C", 195.2f, 721.0f, IzdeBlockC)
            MapLabel(stringResource(R.string.map_label_sdu_life), 309.8f, 157.0f, IzdeGymLabel, width = 90f)
            MapLabel(stringResource(R.string.map_label_dorm), 317.8f, 686.0f, IzdeDormLabel, width = 90f)

            YouAreHere(modifier = Modifier.offset(x = 96.dp, y = 845.dp))

            places.forEach { place ->
                val isSelected = place.id == selectedId
                val pinSize = if (isSelected) 40.dp else 28.dp

                PlacePin(
                    color = Color(place.colorHex),
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
                        .background(IzdeTextPrimary)
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
            .background(IzdeBlue)
    )
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
fun CampusMapPreview() {
    val previewPlace = Place(
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
    CampusMap(
        places = listOf(previewPlace),
        selectedId = "canteen",
        onPinClick = {},
        modifier = Modifier.fillMaxSize()
    )
}
