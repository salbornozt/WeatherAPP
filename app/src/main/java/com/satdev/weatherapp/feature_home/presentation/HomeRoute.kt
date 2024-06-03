package com.satdev.weatherapp.feature_home.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeRoute(viewModel: HomeViewModel = hiltViewModel(), ) {
    val viewState = viewModel.viewState.collectAsState()

    when(viewState.value){
        is HomeViewState.Loading ->{
            HomeLoadingScreen()
        }
        is HomeViewState.Error -> {
            Text(text = "Error")
        }
        is HomeViewState.Success -> {
            HomeScreen(homeViewState = (viewState.value as HomeViewState.Success).weatherData)
        }
    }

}