package com.satdev.weatherapp.feature_forecast.data.model

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h") val treeHour: Double
)