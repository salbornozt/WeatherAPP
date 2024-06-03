package com.satdev.weatherapp.feature_home.data.repository.dataSourceImpl

import com.satdev.weatherapp.core.api.ApiService
import com.satdev.weatherapp.feature_home.data.model.WeatherApiResponse
import com.satdev.weatherapp.feature_home.data.repository.dataSource.WeatherDataSource
import retrofit2.Response
import javax.inject.Inject

class RemoteWeatherDataSourceImpl @Inject constructor(private val apiService: ApiService) : WeatherDataSource {
    override suspend fun getWeather(lat: String, long: String): Response<WeatherApiResponse> {
        return apiService.getWeather(lat, long)
    }
}