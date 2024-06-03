package com.satdev.weatherapp.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satdev.weatherapp.ui.theme.WeatherAPPTheme

@Composable
fun AppLoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp), // Customize the size
            color = MaterialTheme.colorScheme.primary // Customize the color
        )
    }
}

@Preview
@Composable
fun HomeLoadingScreenPreview() {
    WeatherAPPTheme {
        Surface {
            AppLoadingScreen()
        }
    }
}