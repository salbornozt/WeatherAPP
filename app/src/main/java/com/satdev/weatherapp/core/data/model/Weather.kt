package com.satdev.weatherapp.core.data.model

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)