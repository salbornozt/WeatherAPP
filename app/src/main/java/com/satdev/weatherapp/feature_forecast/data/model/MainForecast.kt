package com.satdev.weatherapp.feature_forecast.data.model

import com.google.gson.annotations.SerializedName

data class MainForecast(
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("grnd_level") val groundLevel: Int,
    val humidity: Int,
    val pressure: Int,
    @SerializedName("sea_level") val seaLevel: Int,
    @SerializedName("temp") val temperature: Double,
    @SerializedName("temp_kf") val tempKf: Double,
    @SerializedName("temp_max") val maximumTemperature: Double,
    @SerializedName("temp_min") val minimumTemperature: Double
)