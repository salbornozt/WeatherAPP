package com.satdev.weatherapp.feature_forecast.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satdev.weatherapp.core.wrapper.ApiResult
import com.satdev.weatherapp.feature_forecast.domain.ForecastRepository
import com.satdev.weatherapp.feature_forecast.domain.model.ForecastItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(private val forecastRepository: ForecastRepository) :
    ViewModel() {
    private val forecastViewState: MutableStateFlow<ForecastViewState> =
        MutableStateFlow(ForecastViewState.Loading)
    val viewState: StateFlow<ForecastViewState> = forecastViewState.asStateFlow()

    init {
        viewModelScope.launch {
            val result = forecastRepository.getForecastList("4.751426", "-74.029734")
            when (result) {
                is ApiResult.Error -> {
                    forecastViewState.value = ForecastViewState.Error("Error")
                }

                is ApiResult.Success -> {
                    result.data?.let {
                        forecastViewState.value = ForecastViewState.Succes(forecastData = it, false)
                    }
                }
            }
        }
    }
}

sealed class ForecastViewState {
    data object Loading : ForecastViewState()
    data class Error(val error: String) : ForecastViewState()

    data class Succes(val forecastData: List<ForecastItemModel>, val isRefreshing: Boolean) :
        ForecastViewState()
}