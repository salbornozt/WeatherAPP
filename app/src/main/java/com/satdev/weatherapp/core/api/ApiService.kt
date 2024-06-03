package com.satdev.weatherapp.core.api

import com.satdev.weatherapp.feature_forecast.data.model.ForecastApiResponse
import com.satdev.weatherapp.feature_home.data.model.WeatherApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(WEATHER_API_CALL)
    suspend fun getWeather(@Query("lat") lat:String, @Query("lon")  long:String) : Response<WeatherApiResponse>

    @GET(FORECAST_API_CALL)
    suspend fun getForecast(@Query("lat") lat:String, @Query("lon")  long:String) : Response<ForecastApiResponse>
}