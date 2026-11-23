package com.sdu.izdesdu.presentation.weather

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.sdu.izdesdu.R
import com.sdu.izdesdu.domain.model.CampusWeather
import com.sdu.izdesdu.domain.model.HourlyWeather
import com.sdu.izdesdu.domain.model.WeatherCondition
import com.sdu.izdesdu.domain.model.WeatherErrorType
import com.sdu.izdesdu.presentation.theme.IzdeBlue
import com.sdu.izdesdu.presentation.theme.IzdeBlueHalo
import com.sdu.izdesdu.presentation.theme.IzdeError
import com.sdu.izdesdu.presentation.theme.IzdeErrorBg
import com.sdu.izdesdu.presentation.theme.IzdeOutline
import com.sdu.izdesdu.presentation.theme.IzdeSurfaceCard
import com.sdu.izdesdu.presentation.theme.IzdeTextMuted
import com.sdu.izdesdu.presentation.theme.IzdeTextPrimary
import com.sdu.izdesdu.presentation.theme.IzdeTextSecondary
import com.sdu.izdesdu.presentation.theme.IzdesduTheme

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
            .background(IzdeSurfaceCard)
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
private fun WeatherTopBar(
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    refreshEnabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Surface(
            onClick = onBack,
            modifier = Modifier.size(44.dp),
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(1.dp, IzdeOutline)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.route_back),
                    tint = IzdeTextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Text(
            text = stringResource(R.string.weather_screen_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = IzdeTextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Surface(
            onClick = onRefresh,
            enabled = refreshEnabled,
            modifier = Modifier.size(44.dp),
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(1.dp, IzdeOutline)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = stringResource(R.string.weather_refresh),
                    tint = if (refreshEnabled) IzdeTextPrimary else IzdeTextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun WeatherLoadingState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            CircularProgressIndicator(color = IzdeBlue)
            Text(
                text = stringResource(R.string.weather_loading),
                style = MaterialTheme.typography.bodyMedium,
                color = IzdeTextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun WeatherErrorState(
    errorType: WeatherErrorType,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            border = BorderStroke(1.dp, IzdeOutline)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = IzdeErrorBg,
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = null,
                            tint = IzdeError,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.weather_error_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = IzdeTextPrimary,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(errorType.messageRes()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = IzdeTextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = onRetry,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IzdeBlue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.weather_retry),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherEmptyState(
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            border = BorderStroke(1.dp, IzdeOutline)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = IzdeBlueHalo,
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            tint = IzdeBlue,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.weather_empty_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = IzdeTextPrimary,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(R.string.weather_empty_desc),
                    style = MaterialTheme.typography.bodyMedium,
                    color = IzdeTextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedButton(
                    onClick = onRefresh,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.5.dp, IzdeOutline),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = IzdeTextPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.weather_refresh),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherSuccessState(
    weather: CampusWeather,
    modifier: Modifier = Modifier
) {
    val conditionText = stringResource(weather.condition.labelRes())

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = IzdeBlue
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = stringResource(R.string.weather_campus_location),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (weather.updatedTime.isNotBlank()) {
                        Text(
                            text = stringResource(R.string.weather_updated_at, weather.updatedTime),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f),
                            maxLines = 1
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.weather_temp_value, weather.temperatureC),
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1
                        )
                        Text(
                            text = conditionText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.92f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.size(88.dp)
                    ) {
                        WeatherRemoteIcon(
                            iconUrl = weather.iconUrl,
                            contentDescription = conditionText,
                            placeholderColor = Color.White.copy(alpha = 0.16f),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            WeatherMetricCard(
                label = stringResource(R.string.weather_feels_like_label),
                value = stringResource(R.string.weather_temp_value, weather.feelsLikeC),
                modifier = Modifier.weight(1f)
            )
            WeatherMetricCard(
                label = stringResource(R.string.weather_wind_label),
                value = stringResource(R.string.weather_wind_value, weather.windSpeedKmh),
                modifier = Modifier.weight(1f)
            )
            WeatherMetricCard(
                label = stringResource(R.string.weather_humidity_label),
                value = stringResource(R.string.weather_humidity_value, weather.humidityPercent),
                modifier = Modifier.weight(1f)
            )
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(1.dp, IzdeOutline)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = stringResource(R.string.weather_advice_title),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = IzdeTextPrimary
                )
                Text(
                    text = stringResource(weather.condition.campusAdviceRes(weather.temperatureC)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = IzdeTextSecondary
                )
            }
        }

        Text(
            text = stringResource(R.string.weather_hourly_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = IzdeTextPrimary
        )

        LazyRow(
            contentPadding = PaddingValues(end = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(
                items = weather.hourly,
                key = { index, item -> "${index}_${item.timeLabel}" }
            ) { _, item ->
                HourlyWeatherCard(item = item)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun WeatherMetricCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = BorderStroke(1.dp, IzdeOutline)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = IzdeTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = IzdeTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun HourlyWeatherCard(
    item: HourlyWeather
) {
    val label = stringResource(item.condition.labelRes())

    Surface(
        modifier = Modifier.width(88.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, IzdeOutline)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = item.timeLabel,
                style = MaterialTheme.typography.labelMedium,
                color = IzdeTextSecondary
            )

            Surface(
                shape = CircleShape,
                color = IzdeBlueHalo,
                modifier = Modifier.size(46.dp)
            ) {
                WeatherRemoteIcon(
                    iconUrl = item.iconUrl,
                    contentDescription = label,
                    placeholderColor = IzdeBlue.copy(alpha = 0.14f),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                )
            }

            Text(
                text = stringResource(R.string.weather_temp_value, item.temperatureC),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = IzdeTextPrimary
            )
        }
    }
}

@Composable
private fun WeatherRemoteIcon(
    iconUrl: String,
    contentDescription: String,
    placeholderColor: Color,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(iconUrl)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp)
                    .clip(CircleShape)
                    .background(placeholderColor)
            )
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp)
                    .clip(CircleShape)
                    .background(placeholderColor)
            )
        },
        modifier = modifier
    )
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
