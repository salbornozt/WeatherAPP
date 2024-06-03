package com.satdev.weatherapp.feature_home.domain

import com.satdev.weatherapp.core.wrapper.ApiResult
import com.satdev.weatherapp.feature_home.domain.model.HomeModel
import com.satdev.weatherapp.feature_home.domain.model.WeatherModel

interface WeatherRepository {
    suspend fun getWeather(lat:String,long: String) : ApiResult<HomeModel>
}