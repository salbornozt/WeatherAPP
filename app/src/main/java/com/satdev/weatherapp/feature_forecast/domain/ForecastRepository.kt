package com.satdev.weatherapp.feature_forecast.domain

import com.satdev.weatherapp.core.wrapper.ApiResult
import com.satdev.weatherapp.feature_forecast.domain.model.ForecastItemModel

interface ForecastRepository {
    suspend fun getForecastList(lat:String, long:String) : ApiResult<List<ForecastItemModel>>
}