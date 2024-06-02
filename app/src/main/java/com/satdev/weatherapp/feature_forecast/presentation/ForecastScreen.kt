package com.satdev.weatherapp.feature_forecast.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satdev.weatherapp.ui.theme.WeatherAPPTheme

@Composable
fun ForecastScreen(modifier: Modifier = Modifier) {
    val list = (1..15).toList()
    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(5.dp)) {
        items(list) {
            ForecastItem()
        }
    }
}

@Composable
fun ForecastItem(modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth(), shape = RectangleShape) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(top = 2.dp, start = 5.dp, bottom = 5.dp, end = 2.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Friday, November 18",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ForecastItemTemperature()
                ForecastItemWindSpeed()
                ForecastItemIconDescription()
            }
        }

    }
}

@Composable
fun ForecastItemTemperature() {
    Column {
        Text(
            text = "Hi temp : 58",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Low Temp : 58",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }

}

@Composable
fun ForecastItemWindSpeed() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Wind Speed",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = "19 mph",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = "NW",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun ForecastItemIconDescription() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = Icons.Default.Info, contentDescription = "")
        Text(
            text = "slightly cloudy",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview
@Composable
fun ForecastScreenPreview() {
    WeatherAPPTheme {
        Surface {
            ForecastScreen()
        }
    }
}