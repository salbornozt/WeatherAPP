package com.satdev.weatherapp.navigation

import android.Manifest
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.satdev.weatherapp.feature_forecast.presentation.ForecastRoute
import com.satdev.weatherapp.feature_home.presentation.HomeRoute

@Composable
fun AppNavigation(modifier: Modifier = Modifier, navController : NavHostController) {
    NavHost(modifier = modifier,navController = navController, startDestination = ScreenDestinations.Home.route) {
        composable(ScreenDestinations.Home.route) { HomeRoute() }
        composable(ScreenDestinations.Forecast.route) { ForecastRoute() }
    }
}