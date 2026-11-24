package com.sdu.izdesdu.presentation.weather.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sdu.izdesdu.R
import com.sdu.izdesdu.domain.model.HourlyWeather
import com.sdu.izdesdu.presentation.components.WeatherRemoteIcon
import com.sdu.izdesdu.presentation.theme.IzdeBlue
import com.sdu.izdesdu.presentation.theme.IzdeBlueHalo
import com.sdu.izdesdu.presentation.theme.IzdeOutline
import com.sdu.izdesdu.presentation.theme.IzdeTextPrimary
import com.sdu.izdesdu.presentation.theme.IzdeTextSecondary
import com.sdu.izdesdu.presentation.weather.labelRes

@Composable
fun WeatherHourlySection(
    hourly: List<HourlyWeather>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
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
                items = hourly,
                key = { index, item -> "${index}_${item.timeLabel}" }
            ) { _, item ->
                HourlyWeatherCard(item = item)
            }
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
