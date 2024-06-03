package com.satdev.weatherapp.feature_forecast.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.satdev.weatherapp.R
import com.satdev.weatherapp.core.api.IMAGE_BASE_URL
import com.satdev.weatherapp.feature_forecast.domain.model.ForecastItemModel
import com.satdev.weatherapp.feature_home.domain.model.WeatherWindModel
import com.satdev.weatherapp.feature_home.domain.model.getWindDirection
import com.satdev.weatherapp.ui.theme.WeatherAPPTheme

@Composable
fun ForecastScreen(modifier: Modifier = Modifier, forecastList: List<ForecastItemModel>) {

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(forecastList) { forecastItem ->
            ForecastItem(forecastItem = forecastItem)
        }
    }
}

@Composable
fun ForecastItem(modifier: Modifier = Modifier, forecastItem: ForecastItemModel) {
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
                text = forecastItem.date,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 8.dp, end = 2.dp, bottom = 10.dp)){
                ForecastItemTemperature(
                    modifier = Modifier.align(Alignment.CenterStart),
                    highTemp = forecastItem.highTemp,
                    lowTemp = forecastItem.lowTemp
                )
                ForecastItemWindSpeed(modifier = Modifier.align(Alignment.Center), windModel = forecastItem.windModel)
                ForecastItemIconDescription(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    description = forecastItem.weatherDescription,
                    iconId = forecastItem.iconId
                )
            }
        }

    }
}

@Composable
fun ForecastItemTemperature(modifier: Modifier = Modifier, highTemp: Double, lowTemp: Double) {
    Column(modifier) {
        Text(
            text = "Hi temp : $highTemp",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Low Temp : $lowTemp",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }

}

@Composable
fun ForecastItemWindSpeed(modifier: Modifier = Modifier, windModel: WeatherWindModel) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.wind_speed),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = "${windModel.speed} mph",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = windModel.getWindDirection(),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun ForecastItemIconDescription(
    modifier: Modifier = Modifier,
    description: String,
    iconId: String
) {
    val iconUrl = "$IMAGE_BASE_URL$iconId@2x.png"
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(model = iconUrl, contentDescription = "weather icon", modifier = Modifier.size(50.dp))
        Text(
            text = description,
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
            ForecastScreen(forecastList = listOf(ForecastItemModel()))
        }
    }
}