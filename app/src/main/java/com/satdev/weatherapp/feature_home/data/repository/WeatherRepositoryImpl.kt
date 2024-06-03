package com.satdev.weatherapp.feature_home.data.repository

import com.satdev.weatherapp.core.data.repository.dataSource.ForecastDataSource
import com.satdev.weatherapp.core.wrapper.ApiResult
import com.satdev.weatherapp.core.wrapper.ErrorWrapper
import com.satdev.weatherapp.core.wrapper.toApiResult
import com.satdev.weatherapp.feature_forecast.data.model.ForecastApiResponse
import com.satdev.weatherapp.feature_home.data.model.weatherApiResponseToWeatherModel
import com.satdev.weatherapp.feature_home.data.repository.dataSource.WeatherDataSource
import com.satdev.weatherapp.feature_home.domain.WeatherRepository
import com.satdev.weatherapp.feature_home.domain.model.HomeModel
import com.satdev.weatherapp.feature_home.domain.model.WeatherItemModel
import com.satdev.weatherapp.feature_home.domain.model.WeatherModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherDataSource: WeatherDataSource,
    private val forecastDataSource: ForecastDataSource
) : WeatherRepository {
    override suspend fun getWeather(lat: String, long: String): ApiResult<HomeModel> =
        withContext(Dispatchers.IO) {
            val weatherResultDefferred: Deferred<ApiResult<WeatherModel?>> = async {
                weatherDataSource.getWeather(lat = lat, long = long)
                    .toApiResult(::weatherApiResponseToWeatherModel)
            }
            val forecastResultDeffered: Deferred<ApiResult<List<WeatherItemModel>?>> = async {
                forecastDataSource.getForecast(lat = lat, long = long)
                    .toApiResult(::forecastApiResponseToWeatherItemModel)
            }
            val weatherResult = weatherResultDefferred.await()
            val forecastResult = forecastResultDeffered.await()
            if (weatherResult is ApiResult.Error) {
                return@withContext ApiResult.Error(
                    weatherResult.errorWrapper ?: ErrorWrapper.UnknownError
                )
            }
            if (forecastResult is ApiResult.Error) {
                return@withContext ApiResult.Error(
                    weatherResult.errorWrapper ?: ErrorWrapper.UnknownError
                )
            }

            return@withContext ApiResult.Success(
                HomeModel(
                    actualWeatherModel = weatherResult.data ?: WeatherModel(),
                    nextWeatherList = forecastResult.data ?: listOf()
                )
            )

        }

    private fun forecastApiResponseToWeatherItemModel(forecastApiResponse: ForecastApiResponse?): List<WeatherItemModel>? {
        return forecastApiResponse?.list?.map { forecastItem ->
            WeatherItemModel(
                date = forecastItem.textDate,
                iconId = forecastItem.weather.first().icon,
                temperature = forecastItem.main.temperature
            )
        }
    }
}