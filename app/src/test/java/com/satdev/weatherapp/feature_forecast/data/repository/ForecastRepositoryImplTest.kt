package com.satdev.weatherapp.feature_forecast.data.repository

import com.google.common.truth.Truth.assertThat
import com.satdev.weatherapp.core.data.model.Clouds
import com.satdev.weatherapp.core.data.model.Coord
import com.satdev.weatherapp.core.data.model.Weather
import com.satdev.weatherapp.core.data.model.Wind
import com.satdev.weatherapp.core.data.repository.dataSource.ForecastDataSource
import com.satdev.weatherapp.core.wrapper.ApiResult
import com.satdev.weatherapp.core.wrapper.ErrorWrapper
import com.satdev.weatherapp.feature_forecast.data.model.City
import com.satdev.weatherapp.feature_forecast.data.model.ForecastApiResponse
import com.satdev.weatherapp.feature_forecast.data.model.ForecastItem
import com.satdev.weatherapp.feature_forecast.data.model.ForecastSys
import com.satdev.weatherapp.feature_forecast.data.model.MainForecast
import com.satdev.weatherapp.feature_forecast.data.model.Rain
import com.satdev.weatherapp.feature_forecast.domain.model.ForecastItemModel
import com.satdev.weatherapp.feature_home.data.repository.WeatherRepositoryImplTest
import com.satdev.weatherapp.feature_home.domain.model.WeatherWindModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

class ForecastRepositoryImplTest {
    private lateinit var forecastDataSource: ForecastDataSource
    private lateinit var SUT: ForecastRepositoryImpl

    companion object {
        const val LAT = 0.0
        const val LAN = 0.0
        private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val FORECAST_API_ITEM = ForecastItem(
            clouds = Clouds(all = 20),
            dt = 0,
            textDate = simpleDateFormat.format(Date()),
            main = MainForecast(
                feelsLike = 292.88,
                humidity = 56,
                pressure = 1025,
                temperature = 292.88,
                maximumTemperature = 292.88,
                minimumTemperature = 292.88,
                groundLevel = 0,
                seaLevel = 0,
                tempKf = 0.0
            ),
            pop = 1.0,
            rain = Rain(treeHour = 0.56),
            sys = ForecastSys(
                pod = "n"
            ),
            visibility = 1,
            weather = listOf(
                Weather(
                    description = "few clouds",
                    id = 0,
                    main = "Clouds",
                    icon = "02d"
                )
            ),
            wind = Wind(deg = 156, speed = 120.0)
        )
        val FORECAST_API_RESPONSE =
            ForecastApiResponse(
                city = City(
                    coord = Coord(
                        lat = WeatherRepositoryImplTest.LAT,
                        lon = WeatherRepositoryImplTest.LAN
                    ),
                    country = "CO",
                    id = 0,
                    name = "Bogota",
                    population = 2,
                    sunrise = 0,
                    sunset = 0,
                    timezone = 0
                ),
                cnt = 0,
                cod = "200",
                message = 0,
                list = listOf(FORECAST_API_ITEM, FORECAST_API_ITEM)
            )
        val FORECAST_ITEM_MODEL = ForecastItemModel(
            date = FORECAST_API_ITEM.textDate,
            highTemp = FORECAST_API_ITEM.main.maximumTemperature,
            lowTemp = FORECAST_API_ITEM.main.minimumTemperature,
            windModel = WeatherWindModel(
                speed = FORECAST_API_ITEM.wind.speed,
                deg = FORECAST_API_ITEM.wind.deg
            ),
            weatherDescription = FORECAST_API_ITEM.weather.first().description,
            iconId = FORECAST_API_ITEM.weather.first().icon
        )
    }

    @Before
    fun setUp() {
        forecastDataSource = mockk()
        SUT = ForecastRepositoryImpl(forecastDataSource)
    }

    @Test
    fun `get Forecast List returns success`() = runBlocking {
        //Arrange
        forecastDataSourceReturnsSuccess()
        //Act
        val result = SUT.getForecastList(LAT.toString(), LAN.toString())
        //Assert
        coVerify(exactly = 1) {
            forecastDataSource.getForecast(
                WeatherRepositoryImplTest.LAT.toString(),
                WeatherRepositoryImplTest.LAN.toString()
            )
        }
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        assertThat(result.data).isEqualTo(listOf(FORECAST_ITEM_MODEL))
    }

    @Test
    fun `get Forecast List with data source error returns error`() = runBlocking {
        //Arrange
        forecastDataSourceReturnsError()
        //Act
        val result = SUT.getForecastList(LAT.toString(), LAN.toString())
        //Assert
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        assertThat(result.errorWrapper).isInstanceOf(ErrorWrapper.ServiceInternalError::class.java)
    }

    @Test
    fun `get Forecast List with empty data returns success with empty list`() = runBlocking {
        //Arrange
        forecastDataSourceReturnsSuccessEmptyData()
        //Act
        val result = SUT.getForecastList(LAT.toString(), LAN.toString())
        //Assert
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        assertThat(result.data).isEqualTo(listOf<ForecastItemModel>())
    }

    private fun forecastDataSourceReturnsSuccessEmptyData() {
        coEvery {
            forecastDataSource.getForecast(
                LAT.toString(),
                LAN.toString()
            )
        } returns Response.success(FORECAST_API_RESPONSE.copy(list = listOf()))
    }

    private fun forecastDataSourceReturnsError() {
        coEvery {
            forecastDataSource.getForecast(
                LAT.toString(),
                LAN.toString()
            )
        } returns Response.error(401, "".toResponseBody())
    }

    private suspend fun forecastDataSourceReturnsSuccess() {
        coEvery {
            forecastDataSource.getForecast(
                LAT.toString(),
                LAN.toString()
            )
        } returns Response.success(FORECAST_API_RESPONSE)
    }
}