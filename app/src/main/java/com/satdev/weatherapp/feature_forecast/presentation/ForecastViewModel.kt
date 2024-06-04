package com.satdev.weatherapp.feature_forecast.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satdev.weatherapp.R
import com.satdev.weatherapp.core.domain.repository.LocationRepository
import com.satdev.weatherapp.core.domain.repository.StringRepository
import com.satdev.weatherapp.core.wrapper.ApiResult
import com.satdev.weatherapp.core.wrapper.ErrorWrapper
import com.satdev.weatherapp.feature_forecast.domain.ForecastRepository
import com.satdev.weatherapp.feature_forecast.domain.model.ForecastItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository,
    private val stringRepository: StringRepository) :
    ViewModel() {
    private val forecastViewState: MutableStateFlow<ForecastViewState> =
        MutableStateFlow(ForecastViewState.Loading)
    val viewState: StateFlow<ForecastViewState> = forecastViewState.asStateFlow()

    private val _refreshState = MutableStateFlow(false)
    val refreshState: StateFlow<Boolean> = _refreshState.asStateFlow()

    init {
        viewModelScope.launch {
            checkLocationAndForecast()
        }
    }
    fun onRefresh() = viewModelScope.launch {
        _refreshState.value = true
        checkLocationAndForecast()
        _refreshState.value = false
    }



    private suspend fun checkLocationAndForecast(){
        when (val result = locationRepository.getCurrentLocation()) {
            is ApiResult.Error -> {
                handleLocationError(result.errorWrapper ?: ErrorWrapper.UnknownError)
            }
            is ApiResult.Success -> {
                result.data?.let { pairResult ->
                    fetchForecastData(pairResult.first, pairResult.second)
                }
            }
        }
    }
    
    private suspend fun fetchForecastData(latitude: Double, longitude: Double) {
        val result = forecastRepository.getForecastList(latitude.toString(), longitude.toString())
        when (result) {
            is ApiResult.Error -> {
                forecastViewState.value = ForecastViewState.Error(stringRepository.getString(R.string.error_fetching_forecast))
            }
            is ApiResult.Success -> {
                result.data?.let {
                    forecastViewState.value = ForecastViewState.Success(forecastData = it)
                }
            }
        }
    }

    private fun handleLocationError(errorWrapper: ErrorWrapper) {
        when (errorWrapper) {
            ErrorWrapper.ErrorGettingLocation -> forecastViewState.value =
                ForecastViewState.Error(stringRepository.getString(R.string.error_getting_locations))

            ErrorWrapper.NoLocationPermission -> forecastViewState.value =
                ForecastViewState.RequestPermission(listOf())
            else -> {
                forecastViewState.value = ForecastViewState.Error(stringRepository.getString(R.string.unknown_error))
            }
        }
    }

    fun onPermissionGranted() {
        viewModelScope.launch {
            forecastViewState.value = ForecastViewState.Loading
            checkLocationAndForecast()
        }
    }
}

sealed class ForecastViewState {
    data object Loading : ForecastViewState()
    data class Error(val error: String) : ForecastViewState()

    data class Success(val forecastData: List<ForecastItemModel>) :
        ForecastViewState()

    data class RequestPermission(val defaultData: List<ForecastItemModel>) : ForecastViewState()
}