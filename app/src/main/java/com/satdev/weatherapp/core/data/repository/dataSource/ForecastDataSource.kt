package com.satdev.weatherapp.core.data.repository.dataSource

import com.satdev.weatherapp.feature_forecast.data.model.ForecastApiResponse
import retrofit2.Response

interface ForecastDataSource {
    suspend fun getForecast(lat:String, long:String) : Response<ForecastApiResponse>
}