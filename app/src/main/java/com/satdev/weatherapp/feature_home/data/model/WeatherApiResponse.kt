package com.satdev.weatherapp.feature_home.data.model

import com.google.gson.annotations.SerializedName
import com.satdev.weatherapp.core.data.model.Clouds
import com.satdev.weatherapp.core.data.model.Coord
import com.satdev.weatherapp.core.data.model.Weather
import com.satdev.weatherapp.core.data.model.Wind
import com.satdev.weatherapp.feature_home.domain.model.WeatherModel
import com.satdev.weatherapp.feature_home.domain.model.WeatherWindModel

data class WeatherApiResponse(
    @SerializedName("base") val base: String,
    @SerializedName("clouds") val clouds: Clouds,
    @SerializedName("cod") val cod: Int,
    @SerializedName("coord") val coord: Coord,
    @SerializedName("dt") val dt: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: Main,
    @SerializedName("name") val name: String,
    @SerializedName("sys") val sys: Sys,
    @SerializedName("timezone") val timezone: Int,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("wind") val wind: Wind
)

fun weatherApiResponseToWeatherModel(weatherApiResponse: WeatherApiResponse?) : WeatherModel {
    return WeatherModel(
        cityName = weatherApiResponse?.name ?: "",
        mainWeather = weatherApiResponse?.weather?.first()?.main ?: "",
        feelsLike = weatherApiResponse?.main?.feelsLike ?: 0.0,
        highTemp = weatherApiResponse?.main?.maximumTemperature ?: 0.0,
        lowTemp = weatherApiResponse?.main?.minimumTemperature ?: 0.0,
        windModel = WeatherWindModel(
            speed = weatherApiResponse?.wind?.speed ?: 0.0,
            deg = weatherApiResponse?.wind?.deg ?: 0
        )
    )
}