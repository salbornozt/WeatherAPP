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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satdev.weatherapp.ui.theme.WeatherAPPTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(top = 16.dp, start = 2.dp, end = 2.dp)
    ) {
        HomeHeader()
        Spacer(modifier = Modifier.height(32.dp))
        HomeWeatherList()
    }
}

@Composable
fun HomeHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "City Name", style = MaterialTheme.typography.titleMedium)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Wind", style = MaterialTheme.typography.titleMedium)
            Text(text = "Feels like 60")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "NE")
            Text(text = "65/35")
        }
        Text(text = "10 mph", modifier = Modifier.align(alignment = Alignment.Start))
    }
}

@Composable
fun HomeWeatherList(modifier: Modifier = Modifier) {
    val tempList = (1..20).toList()
    LazyColumn(modifier = modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
        items(tempList){
            HomeWeatherItem()
        }
    }
}

@Composable
fun HomeWeatherItem(modifier: Modifier = Modifier) {
    Card(modifier = modifier
        .fillMaxWidth(),
        shape = RectangleShape
        ) {
        Row(
            modifier
                .fillMaxWidth().background(MaterialTheme.colorScheme.primary).padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 24.dp)
                , horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "1:00 pm", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onPrimary)
            ItemWeatherTemperature()
        }
    }
}

@Composable
fun ItemWeatherTemperature(modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(48.dp)) {
        Text(text = "66 F", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onPrimary)
        Icon(imageVector = Icons.Default.Info, contentDescription = "")
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    WeatherAPPTheme {
        Surface {
            HomeScreen()
        }
    }
}