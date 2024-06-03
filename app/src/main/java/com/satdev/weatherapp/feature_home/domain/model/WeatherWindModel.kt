package com.satdev.weatherapp.feature_home.domain.model

data class WeatherWindModel(
    val speed: Double = 0.0,
    val deg: Int = 0
)

fun WeatherWindModel.getWindDirection(): String {
    // Optional handling for negative angles
    val normalizedDegrees = if (this.deg < 0) deg + 360 else deg
    val abbreviations = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
    val index = ((normalizedDegrees % 360) / 45).toInt()
    return abbreviations[index]
}