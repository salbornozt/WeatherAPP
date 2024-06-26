package com.satdev.weatherapp.feature_home.data.model

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("feels_like") val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    @SerializedName("temp") val temperature: Double,
    @SerializedName("temp_max") val maximumTemperature: Double,
    @SerializedName("temp_min") val minimumTemperature: Double
)