package com.satdev.weatherapp.core.data.repository.dataSourceImpl

import com.satdev.weatherapp.core.api.ApiService
import com.satdev.weatherapp.core.data.repository.dataSource.ForecastDataSource
import com.satdev.weatherapp.feature_forecast.data.model.ForecastApiResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteForecastDataSourceImpl @Inject constructor(private val apiService: ApiService) : ForecastDataSource {
    override suspend fun getForecast(lat: String, long: String): Response<ForecastApiResponse> {
        return apiService.getForecast(lat, long)
    }
}