package com.satdev.weatherapp.feature_forecast.data.repository

import com.satdev.weatherapp.core.data.repository.dataSource.ForecastDataSource
import com.satdev.weatherapp.core.wrapper.ApiResult
import com.satdev.weatherapp.core.wrapper.ErrorWrapper
import com.satdev.weatherapp.core.wrapper.toApiResult
import com.satdev.weatherapp.feature_forecast.data.model.ForecastApiResponse
import com.satdev.weatherapp.feature_forecast.domain.ForecastRepository
import com.satdev.weatherapp.feature_forecast.domain.model.ForecastItemModel
import com.satdev.weatherapp.feature_home.domain.model.WeatherWindModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(private val forecastDataSource: ForecastDataSource): ForecastRepository {
    override suspend fun getForecastList(
        lat: String,
        long: String
    ): ApiResult<List<ForecastItemModel>> = withContext(Dispatchers.IO){
        val result = forecastDataSource.getForecast(lat, long).toApiResult(::forecastApiResponseToForecastItemModel)
        if (result is ApiResult.Error){
            return@withContext ApiResult.Error(result.errorWrapper ?: ErrorWrapper.UnknownError)
        }
        ApiResult.Success(groupDailyForecast(result.data ?: listOf()))
    }

    private fun forecastApiResponseToForecastItemModel(forecastApiResponse: ForecastApiResponse?): List<ForecastItemModel>? {
        return forecastApiResponse?.list?.map { forecastItem ->
            ForecastItemModel(
                date = forecastItem.textDate,
                iconId = forecastItem.weather.first().icon,
                highTemp = forecastItem.main.maximumTemperature,
                lowTemp = forecastItem.main.minimumTemperature,
                windModel = WeatherWindModel(speed = forecastItem.wind.speed, deg = forecastItem.wind.deg),
                weatherDescription = forecastItem.weather.first().description
            )
        }
    }
    private fun groupDailyForecast(forecastList: List<ForecastItemModel>): List<ForecastItemModel> {
        val dailyMap = mutableMapOf<String, MutableList<ForecastItemModel>>()

        // Group items by date
        forecastList.forEach { item ->
            val date = item.date.split(" ")[0] // Extract only the date part
            dailyMap.getOrPut(date) { mutableListOf() }.add(item)
        }

        return dailyMap.map { (date, items) ->
            val (descriptionCounts, iconCounts) = items.fold(mutableMapOf<String, Int>() to mutableMapOf<String, Int>()) { (descCounts, iconCounts), item ->
                descCounts[item.weatherDescription] = descCounts.getOrDefault(item.weatherDescription, 0) + 1
                iconCounts[item.iconId] = iconCounts.getOrDefault(item.iconId, 0) + 1
                descCounts to iconCounts
            }

            // Find most common description and icon
            val mostCommonDescription = descriptionCounts.maxByOrNull { it.value }?.key ?: ""
            val mostCommonIcon = iconCounts.maxByOrNull { it.value }?.key ?: ""

            // Create a new item for the day with the most common data
            ForecastItemModel(date, items[0].highTemp, items[0].lowTemp, items[0].windModel, mostCommonDescription, mostCommonIcon)
        }
    }
}