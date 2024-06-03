package com.satdev.weatherapp.feature_home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.satdev.weatherapp.core.api.IMAGE_BASE_URL
import com.satdev.weatherapp.feature_home.domain.model.HomeModel
import com.satdev.weatherapp.feature_home.domain.model.WeatherItemModel
import com.satdev.weatherapp.feature_home.domain.model.WeatherModel
import com.satdev.weatherapp.feature_home.domain.model.getWindDirection
import com.satdev.weatherapp.ui.theme.WeatherAPPTheme
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(modifier: Modifier = Modifier, homeViewState: HomeModel) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 2.dp, end = 2.dp)
    ) {

        HomeHeader(weatherModel = homeViewState.actualWeatherModel)
        Spacer(modifier = Modifier.height(32.dp))
        HomeWeatherList(weatherList = homeViewState.nextWeatherList)
    }
}

@Composable
fun HomeHeader(modifier: Modifier = Modifier, weatherModel: WeatherModel) {
    Column(
        modifier = modifier
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = weatherModel.cityName, style = MaterialTheme.typography.titleMedium)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = weatherModel.mainWeather, style = MaterialTheme.typography.titleMedium)
            Text(text = "Feels like : ${weatherModel.feelsLike}")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = weatherModel.windModel.getWindDirection())
            Text(text = "${weatherModel.highTemp}/${weatherModel.lowTemp}")
        }
        Text(
            text = "${weatherModel.windModel.speed} mph",
            modifier = Modifier.align(alignment = Alignment.Start)
        )
    }
}

@Composable
fun HomeWeatherList(modifier: Modifier = Modifier, weatherList: List<WeatherItemModel>) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(weatherList) { item ->
            HomeWeatherItem(weatherModel = item)
        }
    }
}

@Composable
fun HomeWeatherItem(modifier: Modifier = Modifier, weatherModel: WeatherItemModel) {
    val formattedTime = remember(weatherModel.date) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = inputFormat.parse(weatherModel.date) // Parse input date string
        outputFormat.format(date) // Format as desired (e.g., "HH:mm")
    }
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RectangleShape
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formattedTime,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
            ItemWeatherTemperature(
                temperature = weatherModel.temperature.toString(),
                iconId = weatherModel.iconId
            )
        }
    }
}

@Composable
fun ItemWeatherTemperature(modifier: Modifier = Modifier, temperature: String, iconId: String) {
    val iconUrl = "$IMAGE_BASE_URL$iconId@2x.png"
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(48.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = temperature,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
        AsyncImage(model = iconUrl, contentDescription = "weather icon")
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    WeatherAPPTheme {
        Surface {
            HomeScreen(homeViewState = HomeModel())
        }
    }
}