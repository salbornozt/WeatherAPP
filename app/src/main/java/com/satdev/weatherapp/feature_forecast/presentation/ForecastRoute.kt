package com.satdev.weatherapp.feature_forecast.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.satdev.weatherapp.PermissionState
import com.satdev.weatherapp.PermissionViewModel
import com.satdev.weatherapp.core.ui.AppErrorScreen
import com.satdev.weatherapp.core.ui.AppLoadingScreen
import com.satdev.weatherapp.core.ui.LocationPermissionRationaleScreen
import com.satdev.weatherapp.core.ui.RequestLocationPermission

@Composable
fun ForecastRoute(
    viewModel: ForecastViewModel = hiltViewModel(),
    permissionViewModel: PermissionViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()
    val refreshState by viewModel.refreshState.collectAsState()
    when (viewState) {
        is ForecastViewState.Error -> AppErrorScreen(errorMessage = (viewState as ForecastViewState.Error).error)
        is ForecastViewState.Loading -> AppLoadingScreen()
        is ForecastViewState.Success -> ForecastScreen(
            forecastList = (viewState as ForecastViewState.Success).forecastData,
            isRefreshing = refreshState,
            onRefresh = viewModel::onRefresh
        )
        is ForecastViewState.RequestPermission -> {
            RequestLocationPermission(
                onPermissionGranted = viewModel::onPermissionGranted,
                onPermissionDenied = permissionViewModel::onPermissionDenied,
                onPermissionsRevoked = permissionViewModel::onPermissionDenied,
                shouldShowRationale = permissionViewModel::onShouldShowRationale
            )
            val permissionState = permissionViewModel.permissionState.collectAsState()
            when(permissionState.value){
                PermissionState.ShouldShowRationale -> LocationPermissionRationaleScreen()
                else -> {
                    ForecastScreen(forecastList = listOf(), isRefreshing = refreshState, onRefresh = viewModel::onRefresh)
                }
            }
        }
    }
}