package com.satdev.weatherapp.core.data.model

data class Wind(
    val deg: Int,
    val gust: Double? = null,
    val speed: Double
)