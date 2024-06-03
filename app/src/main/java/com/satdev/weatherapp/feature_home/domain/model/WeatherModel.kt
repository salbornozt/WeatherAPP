package com.satdev.weatherapp.feature_home.domain.model

data class WeatherModel(
    val cityName:String = "",
    val mainWeather:String = "",
    val feelsLike: Double = 0.0,
    val highTemp : Double = 0.0,
    val lowTemp : Double = 0.0,
    val windModel: WeatherWindModel = WeatherWindModel(),
)