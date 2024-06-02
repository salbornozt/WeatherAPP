package com.satdev.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.satdev.weatherapp.feature_forecast.presentation.ForecastRoute
import com.satdev.weatherapp.feature_home.presentation.HomeRoute

@Composable
fun AppNavigation(modifier: Modifier = Modifier, navController : NavHostController) {
    NavHost(modifier = modifier,navController = navController, startDestination = ScreenDestinations.Home.route) {
        composable(ScreenDestinations.Home.route) { HomeRoute() }
        composable(ScreenDestinations.Forecast.route) { ForecastRoute() }
    }
}