package com.satdev.weatherapp.feature_forecast.data.model

import com.google.gson.annotations.SerializedName
import com.satdev.weatherapp.core.data.model.Clouds
import com.satdev.weatherapp.core.data.model.Weather
import com.satdev.weatherapp.core.data.model.Wind

data class ForecastItem(
    val clouds: Clouds,
    val dt: Int,
    @SerializedName("dt_txt") val textDate: String,
    val main: MainForecast,
    val pop: Double,
    val rain: Rain,
    val sys: ForecastSys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)