package com.satdev.weatherapp.feature_forecast.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.satdev.weatherapp.core.ui.AppLoadingScreen

@Composable
fun ForecastRoute(viewModel: ForecastViewModel =  hiltViewModel()) {
    val viewState = viewModel.viewState.collectAsState()
    when(viewState.value){
        is ForecastViewState.Error -> Text(text = "Error")
        ForecastViewState.Loading -> AppLoadingScreen()
        is ForecastViewState.Succes -> ForecastScreen(forecastList = (viewState.value as ForecastViewState.Succes).forecastData)
    }
}