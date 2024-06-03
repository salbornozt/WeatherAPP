package com.satdev.weatherapp.feature_home.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.satdev.weatherapp.PermissionViewModel
import com.satdev.weatherapp.PermissionState
import com.satdev.weatherapp.core.ui.AppErrorScreen
import com.satdev.weatherapp.core.ui.AppLoadingScreen
import com.satdev.weatherapp.core.ui.LocationPermissionRationaleScreen
import com.satdev.weatherapp.core.ui.RequestLocationPermission

@Composable
fun HomeRoute(viewModel: HomeViewModel = hiltViewModel(), permissionViewModel: PermissionViewModel = hiltViewModel()) {
    val viewState = viewModel.viewState.collectAsState()
    when(viewState.value){
        is HomeViewState.Loading ->{
            AppLoadingScreen()
        }
        is HomeViewState.Error -> {
            AppErrorScreen(errorMessage = (viewState.value as HomeViewState.Error).message)
        }
        is HomeViewState.Success -> {
            HomeScreen(homeViewState = (viewState.value as HomeViewState.Success).weatherData)
        }
        is HomeViewState.RequestPermission -> {
            RequestLocationPermission(
                onPermissionGranted = permissionViewModel::onPermissionGranted,
                onPermissionDenied = permissionViewModel::onPermissionDenied,
                onPermissionsRevoked = permissionViewModel::onPermissionDenied,
                shouldShowRationale = permissionViewModel::onShouldShowRationale)
            val permissionState = permissionViewModel.permissionState.collectAsState()
            when(permissionState.value){
                PermissionState.Granted -> {
                    viewModel.checkLocationPermissionAndFetchData()
                }
                PermissionState.ShouldShowRationale -> LocationPermissionRationaleScreen()
                PermissionState.Unknown -> Unit
                else -> {
                    HomeScreen(homeViewState = (viewState.value as HomeViewState.RequestPermission).defaultData)
                }
            }
        }
    }

}