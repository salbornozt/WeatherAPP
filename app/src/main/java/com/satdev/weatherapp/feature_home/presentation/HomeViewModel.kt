package com.satdev.weatherapp.feature_home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satdev.weatherapp.R
import com.satdev.weatherapp.core.domain.repository.LocationRepository
import com.satdev.weatherapp.core.domain.repository.StringRepository
import com.satdev.weatherapp.core.wrapper.ApiResult
import com.satdev.weatherapp.core.wrapper.ErrorWrapper
import com.satdev.weatherapp.feature_home.domain.WeatherRepository
import com.satdev.weatherapp.feature_home.domain.model.HomeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository,
    private val stringRepository: StringRepository
) : ViewModel() {

    private val homeViewState: MutableStateFlow<HomeViewState> =
        MutableStateFlow(HomeViewState.Loading)
    val viewState: StateFlow<HomeViewState> = homeViewState.asStateFlow()

    private val _refreshState = MutableStateFlow(false)
    val refreshState: StateFlow<Boolean> = _refreshState.asStateFlow()

    init {
        viewModelScope.launch {
            checkLocationPermissionAndFetchData()
        }
    }

    fun onRefresh() = viewModelScope.launch {
        _refreshState.value = true
        checkLocationPermissionAndFetchData()
        _refreshState.value = false
    }

    private suspend fun checkLocationPermissionAndFetchData() {
        when (val result = locationRepository.getCurrentLocation()) {
            is ApiResult.Error -> {
                handleLocationError(result.errorWrapper ?: ErrorWrapper.UnknownError)
            }

            is ApiResult.Success -> {
                result.data?.let { pairResult ->
                    fetchWeatherData(pairResult.first, pairResult.second)
                }
            }
        }
    }

    private fun handleLocationError(errorWrapper: ErrorWrapper) {
        when (errorWrapper) {
            ErrorWrapper.ErrorGettingLocation -> homeViewState.value =
                HomeViewState.Error(stringRepository.getString(R.string.error_getting_locations))

            ErrorWrapper.NoLocationPermission -> homeViewState.value =
                HomeViewState.RequestPermission(HomeModel())

            else -> {
                homeViewState.value =
                    HomeViewState.Error(stringRepository.getString(R.string.unknown_error))
            }
        }
    }

    private suspend fun fetchWeatherData(latitude: Double, longitude: Double) {
        when (val result =
            weatherRepository.getWeather(latitude.toString(), longitude.toString())) {
            is ApiResult.Error -> {
                homeViewState.value =
                    HomeViewState.Error(stringRepository.getString(R.string.error_fetching_weather))
            }

            is ApiResult.Success -> {
                result.data?.let {
                    homeViewState.value = HomeViewState.Success(it, false)
                }
            }
        }
    }

    fun onPermissionGranted() {
        Log.d("sat_tag", "onPermissionGranted: ")
        viewModelScope.launch {
            homeViewState.value = HomeViewState.Loading
            checkLocationPermissionAndFetchData()
        }
    }

}

sealed class HomeViewState {
    data object Loading : HomeViewState()
    data class Success(val weatherData: HomeModel, val isRefreshing: Boolean) : HomeViewState()
    data class RequestPermission(val defaultData: HomeModel) : HomeViewState()
    data class Error(val message: String) : HomeViewState()
}