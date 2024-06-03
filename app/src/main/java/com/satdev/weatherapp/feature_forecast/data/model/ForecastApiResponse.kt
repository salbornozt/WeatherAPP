package com.satdev.weatherapp.feature_forecast.data.model

data class ForecastApiResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ForecastItem>,
    val message: Int
)