package com.satdev.weatherapp.feature_home.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satdev.weatherapp.core.wrapper.ApiResult
import com.satdev.weatherapp.feature_home.domain.WeatherRepository
import com.satdev.weatherapp.feature_home.domain.model.HomeModel
import com.satdev.weatherapp.feature_home.domain.model.WeatherModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val homeViewState : MutableStateFlow<HomeViewState> = MutableStateFlow(HomeViewState.Loading)
    val viewState : StateFlow<HomeViewState> = homeViewState.asStateFlow()

    init {
        viewModelScope.launch {
            when(val result = weatherRepository.getWeather("4.751426","-74.029734")){
                is ApiResult.Error -> {
                    homeViewState.value = HomeViewState.Error("Error")
                }
                is ApiResult.Success -> {
                    result.data?.let {
                        homeViewState.value = HomeViewState.Success(it, false)
                    }
                }
            }
        }
    }

}

sealed class HomeViewState {
    data object Loading : HomeViewState()
    data class Success(val weatherData: HomeModel, val isRefreshing : Boolean) : HomeViewState()
    data class Error(val message: String) : HomeViewState()
}