package com.satdev.weatherapp.feature_forecast.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.satdev.weatherapp.PermissionState
import com.satdev.weatherapp.PermissionViewModel
import com.satdev.weatherapp.core.ui.AppErrorScreen
import com.satdev.weatherapp.core.ui.AppLoadingScreen
import com.satdev.weatherapp.core.ui.LocationPermissionRationaleScreen
import com.satdev.weatherapp.core.ui.RequestLocationPermission
import com.satdev.weatherapp.feature_home.presentation.HomeScreen
import com.satdev.weatherapp.feature_home.presentation.HomeViewState

@Composable
fun ForecastRoute(viewModel: ForecastViewModel =  hiltViewModel(), permissionViewModel : PermissionViewModel = hiltViewModel()) {
    val viewState = viewModel.viewState.collectAsState()
    when(viewState.value){
        is ForecastViewState.Error -> AppErrorScreen(errorMessage = (viewState.value as ForecastViewState.Error).error)
        ForecastViewState.Loading -> AppLoadingScreen()
        is ForecastViewState.Success -> ForecastScreen(forecastList = (viewState.value as ForecastViewState.Success).forecastData)
        is ForecastViewState.RequestPermission -> {
            RequestLocationPermission(
                onPermissionGranted = permissionViewModel::onPermissionGranted,
                onPermissionDenied = permissionViewModel::onPermissionDenied,
                onPermissionsRevoked = permissionViewModel::onPermissionDenied,
                shouldShowRationale = permissionViewModel::onShouldShowRationale)
            val permissionState = permissionViewModel.permissionState.collectAsState()
            when(permissionState.value){
                PermissionState.Granted -> {
                    viewModel.checkLocationAndFetchForecast()
                }
                PermissionState.ShouldShowRationale -> LocationPermissionRationaleScreen()
                PermissionState.Unknown -> Unit
                else -> {
                    ForecastScreen(forecastList = (viewState.value as ForecastViewState.RequestPermission).defaultData)
                }
            }
        }
    }
}