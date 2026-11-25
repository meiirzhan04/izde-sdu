package com.sdu.izdesdu.presentation.map

import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sdu.izdesdu.domain.model.CampusWeather
import com.sdu.izdesdu.domain.model.Place
import com.sdu.izdesdu.domain.model.WeatherCondition
import com.sdu.izdesdu.presentation.components.CampusMap
import com.sdu.izdesdu.presentation.components.MapHeader
import com.sdu.izdesdu.presentation.components.PlacesSheet
import com.sdu.izdesdu.presentation.components.WeatherMapWidget
import com.sdu.izdesdu.presentation.weather.WeatherUiState

@Composable
fun MapScreenContent(
    uiState: MapUiState,
    weatherState: WeatherUiState,
    onSelectPlace: (String) -> Unit,
    onStartRoute: (String) -> Unit,
    onOpenPlace: (String) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenWeather: () -> Unit,
    onRetryWeather: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            CampusMap(
                places = uiState.places,
                selectedId = uiState.selectedId,
                onPinClick = onSelectPlace,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MapHeader(
                    onOpenSettings = onOpenSettings,
                    totalScore = uiState.totalScore,
                    modifier = Modifier.fillMaxWidth()
                )
                WeatherMapWidget(
                    weatherState = weatherState,
                    onOpenWeather = onOpenWeather,
                    onRetry = onRetryWeather
                )
            }
        }

        PlacesSheet(
            sortOrder = uiState.sortOrder,
            places = uiState.places,
            selected = uiState.selectedPlace,
            onSelectPlace = onSelectPlace,
            onStartRoute = onStartRoute,
            onOpenPlace = onOpenPlace
        )
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun MapScreenContentPreview() {
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
    MapScreenContent(
        uiState = MapUiState(
            places = previewPlaces,
            selectedId = "canteen",
            totalScore = 20
        ),
        weatherState = WeatherUiState.Success(
            CampusWeather(
                temperatureC = 14,
                feelsLikeC = 12,
                windSpeedKmh = 8,
                humidityPercent = 45,
                condition = WeatherCondition.CLEAR,
                iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
                isDay = true,
                updatedTime = "13:00",
                hourly = emptyList()
            )
        ),
        onSelectPlace = {},
        onStartRoute = {},
        onOpenPlace = {},
        onOpenSettings = {},
        onOpenWeather = {},
        onRetryWeather = {}
    )
}
