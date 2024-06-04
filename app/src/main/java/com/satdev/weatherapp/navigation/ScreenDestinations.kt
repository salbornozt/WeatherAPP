package com.satdev.weatherapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.satdev.weatherapp.R
import com.satdev.weatherapp.ui.navigation.Route.FORECAST
import com.satdev.weatherapp.ui.navigation.Route.HOME

sealed class ScreenDestinations(val route: String, val resourceId: Int, val icon : ImageVector) {
    data object Home : ScreenDestinations(HOME, R.string.home_nav, Icons.Default.Home)
    data object Forecast : ScreenDestinations(FORECAST, R.string.forecast_nav,Icons.Default.DateRange)
}