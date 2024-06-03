package com.satdev.weatherapp.feature_forecast.data.model

import com.satdev.weatherapp.core.data.model.Coord

data class City(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)