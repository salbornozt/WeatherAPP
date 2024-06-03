package com.satdev.weatherapp.feature_home.domain.model

data class HomeModel(
    val actualWeatherModel: WeatherModel = WeatherModel(),
    val nextWeatherList : List<WeatherItemModel> = listOf()
)
