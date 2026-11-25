package com.sdu.izdesdu.presentation.weather

import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sdu.izdesdu.domain.model.CampusWeather
import com.sdu.izdesdu.domain.model.HourlyWeather
import com.sdu.izdesdu.domain.model.WeatherCondition
import com.sdu.izdesdu.domain.model.WeatherErrorType
import com.sdu.izdesdu.presentation.theme.IzdesduTheme
import com.sdu.izdesdu.presentation.weather.components.WeatherAdviceCard
import com.sdu.izdesdu.presentation.weather.components.WeatherEmptyState
import com.sdu.izdesdu.presentation.weather.components.WeatherErrorState
import com.sdu.izdesdu.presentation.weather.components.WeatherHeroCard
import com.sdu.izdesdu.presentation.weather.components.WeatherHourlySection
import com.sdu.izdesdu.presentation.weather.components.WeatherLoadingState
import com.sdu.izdesdu.presentation.weather.components.WeatherMetricsRow
import com.sdu.izdesdu.presentation.weather.components.WeatherTopBar

@Composable
fun WeatherScreenContent(
    uiState: WeatherUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        WeatherTopBar(
            onBack = onBack,
            onRefresh = onRetry,
            refreshEnabled = uiState !is WeatherUiState.Loading
        )

        when (uiState) {
            WeatherUiState.Loading -> WeatherLoadingState(
                modifier = Modifier.weight(1f)
            )
            is WeatherUiState.Success -> WeatherSuccessState(
                weather = uiState.weather,
                modifier = Modifier.weight(1f)
            )
            is WeatherUiState.Error -> WeatherErrorState(
                errorType = uiState.errorType,
                onRetry = onRetry,
                modifier = Modifier.weight(1f)
            )
            WeatherUiState.Empty -> WeatherEmptyState(
                onRefresh = onRetry,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun WeatherSuccessState(
    weather: CampusWeather,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WeatherHeroCard(weather = weather)
        WeatherMetricsRow(weather = weather)
        WeatherAdviceCard(weather = weather)
        WeatherHourlySection(hourly = weather.hourly)
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun WeatherScreenSuccessPreview() {
    val previewWeather = CampusWeather(
        temperatureC = 14,
        feelsLikeC = 12,
        windSpeedKmh = 9,
        humidityPercent = 48,
        condition = WeatherCondition.PARTLY_CLOUDY,
        iconUrl = "https://openweathermap.org/img/wn/02d@2x.png",
        isDay = true,
        updatedTime = "13:00",
        hourly = listOf(
            HourlyWeather("13:00", 14, WeatherCondition.PARTLY_CLOUDY, "https://openweathermap.org/img/wn/02d@2x.png"),
            HourlyWeather("14:00", 15, WeatherCondition.CLEAR, "https://openweathermap.org/img/wn/01d@2x.png"),
            HourlyWeather("15:00", 15, WeatherCondition.CLEAR, "https://openweathermap.org/img/wn/01d@2x.png"),
            HourlyWeather("16:00", 13, WeatherCondition.CLOUDY, "https://openweathermap.org/img/wn/04d@2x.png")
        )
    )
    IzdesduTheme {
        WeatherScreenContent(
            uiState = WeatherUiState.Success(previewWeather),
            onBack = {},
            onRetry = {}
        )
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun WeatherScreenErrorPreview() {
    IzdesduTheme {
        WeatherScreenContent(
            uiState = WeatherUiState.Error(WeatherErrorType.NETWORK),
            onBack = {},
            onRetry = {}
        )
    }
}
