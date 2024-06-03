package com.satdev.weatherapp.feature_forecast.domain.model

import com.satdev.weatherapp.feature_home.domain.model.WeatherWindModel

data class ForecastItemModel(
    val date: String = "",
    val highTemp : Double = 0.0,
    val lowTemp : Double = 0.0,
    val windModel: WeatherWindModel = WeatherWindModel(),
    val weatherDescription : String = "",
    val iconId : String = ""

)