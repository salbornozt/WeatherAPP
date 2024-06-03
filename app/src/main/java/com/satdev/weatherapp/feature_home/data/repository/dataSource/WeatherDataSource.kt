package com.satdev.weatherapp.feature_home.data.repository.dataSource

import com.satdev.weatherapp.feature_home.data.model.WeatherApiResponse
import retrofit2.Response

interface WeatherDataSource {
    suspend fun getWeather(lat:String, long:String) : Response<WeatherApiResponse>
}