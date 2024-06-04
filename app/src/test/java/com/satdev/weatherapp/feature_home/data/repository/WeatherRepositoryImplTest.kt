package com.satdev.weatherapp.feature_home.data.repository

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
import com.satdev.weatherapp.feature_home.data.model.Main
import com.satdev.weatherapp.feature_home.data.model.Sys
import com.satdev.weatherapp.feature_home.data.model.WeatherApiResponse
import com.satdev.weatherapp.feature_home.data.repository.dataSource.WeatherDataSource
import com.satdev.weatherapp.feature_home.domain.model.HomeModel
import com.satdev.weatherapp.feature_home.domain.model.WeatherItemModel
import com.satdev.weatherapp.feature_home.domain.model.WeatherModel
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
import java.util.Calendar
import java.util.Date

class WeatherRepositoryImplTest {
    private lateinit var weatherDataSource: WeatherDataSource
    private lateinit var forecastDataSource: ForecastDataSource
    private lateinit var SUT: WeatherRepositoryImpl

    companion object {
        const val LAT = 0.0
        const val LAN = 0.0
        private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val WEATHER_API_RESPONSE = WeatherApiResponse(
            base = "stations",
            clouds = Clouds(all = 20),
            cod = 200,
            coord = Coord(lat = LAT, LAN),
            dt = 1717449130,
            id = 3688689,
            main = Main(
                feelsLike = 292.88,
                humidity = 56,
                pressure = 1025,
                temperature = 292.88,
                maximumTemperature = 292.88,
                minimumTemperature = 292.88
            ),
            name = "Bogota",
            sys = Sys(
                country = "CO",
                id = 0,
                sunrise = 0,
                sunset = 0,
                type = 0
            ),
            timezone = 0,
            visibility = 0,
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
                    coord = Coord(lat = LAT, lon = LAN),
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
        val ACTUAL_WEATHER = WeatherModel(
            cityName = WEATHER_API_RESPONSE.name,
            mainWeather = WEATHER_API_RESPONSE.weather.first().main,
            feelsLike = WEATHER_API_RESPONSE.main.feelsLike,
            highTemp = WEATHER_API_RESPONSE.main.maximumTemperature,
            lowTemp = WEATHER_API_RESPONSE.main.minimumTemperature,
            windModel = WeatherWindModel(
                speed = WEATHER_API_RESPONSE.wind.speed,
                deg = WEATHER_API_RESPONSE.wind.deg
            )
        )
        val WEATHER_ITEM_MODEL = WeatherItemModel(
            date = FORECAST_API_ITEM.textDate,
            temperature = FORECAST_API_ITEM.main.temperature,
            iconId = FORECAST_API_ITEM.weather.first().icon
        )
        val NEXT_WEATHER = listOf(
            WEATHER_ITEM_MODEL, WEATHER_ITEM_MODEL
        )
        val HOME_WEATHER_RESULT = HomeModel(ACTUAL_WEATHER, NEXT_WEATHER)
    }

    @Before
    fun setUp() {
        weatherDataSource = mockk()
        forecastDataSource = mockk()
        SUT = WeatherRepositoryImpl(weatherDataSource, forecastDataSource)
    }

    @Test
    fun `get weather should return home model success`() = runBlocking {
        //Arrange
        weatherDataSourceReturnsSuccess()
        forecastDataSourceReturnsSuccess()
        //Act
        val result = SUT.getWeather(LAT.toString(), LAN.toString())
        //Assert
        coVerify(exactly = 1) {
            weatherDataSource.getWeather(LAT.toString(), LAN.toString())
            forecastDataSource.getForecast(LAT.toString(), LAN.toString())
        }
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        val data = result.data
        assertThat(data).isEqualTo(HOME_WEATHER_RESULT)
    }

    @Test
    fun `get weather with weather data and empty forecast data return success`() = runBlocking {
        //Arrange
        weatherDataSourceReturnsSuccess()
        forecastDataSourceReturnsSuccessEmptyData()
        //Act
        val result = SUT.getWeather(LAT.toString(), LAN.toString())
        //Assert
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        val data = result.data
        assertThat(data).isEqualTo(HOME_WEATHER_RESULT.copy(nextWeatherList = listOf()))
    }

    @Test
    fun `get weather should return home model and only today list success`() = runBlocking {
        //Arrange
        weatherDataSourceReturnsSuccess()
        val tomorrow = getFutureDate()
        val apiResponse = FORECAST_API_RESPONSE.copy(
            list = listOf(
                FORECAST_API_ITEM,
                FORECAST_API_ITEM.copy(textDate = simpleDateFormat.format(tomorrow))
            )
        )
        forecastDataSourceReturnsSuccessListWithFutureDays(apiResponse)
        //Act
        val result = SUT.getWeather(LAT.toString(), LAN.toString())
        //Assert
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        val data = result.data
        assertThat(data).isEqualTo(
            HOME_WEATHER_RESULT.copy(
                nextWeatherList = listOf(
                    WEATHER_ITEM_MODEL
                )
            )
        )
    }

    @Test
    fun `get weather with error weather data and forecast data return error`() = runBlocking {
        //Arrange
        weatherDataSourceReturnsError()
        forecastDataSourceReturnsSuccess()
        //Act
        val result = SUT.getWeather(LAT.toString(), LAN.toString())
        //Assert
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        val error = result.errorWrapper
        assertThat(error).isInstanceOf(ErrorWrapper.ServiceInternalError::class.java)
    }

    @Test
    fun `get weather with weather data and error forecast data return error`() = runBlocking {
        //Arrange
        weatherDataSourceReturnsSuccess()
        forecastDataSourceReturnsError()
        //Act
        val result = SUT.getWeather(LAT.toString(), LAN.toString())
        //Assert
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        val error = result.errorWrapper
        assertThat(error).isInstanceOf(ErrorWrapper.ServiceInternalError::class.java)
    }

    @Test
    fun `get weather with error weather data and error forecast data return error`() = runBlocking {
        //Arrange
        weatherDataSourceReturnsError()
        forecastDataSourceReturnsError()
        //Act
        val result = SUT.getWeather(LAT.toString(), LAN.toString())
        //Assert
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        val error = result.errorWrapper
        assertThat(error).isInstanceOf(ErrorWrapper.ServiceInternalError::class.java)
    }

    private fun forecastDataSourceReturnsError() {
        coEvery {
            forecastDataSource.getForecast(
                LAT.toString(),
                LAN.toString()
            )
        } returns Response.error(401, "".toResponseBody())
    }

    private fun weatherDataSourceReturnsError() {
        coEvery {
            weatherDataSource.getWeather(
                LAT.toString(),
                LAN.toString()
            )
        } returns Response.error(401, "".toResponseBody())
    }


    private fun getFutureDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1) // Add one day to the current date
        val tomorrow = calendar.time
        return tomorrow
    }

    private fun forecastDataSourceReturnsSuccessEmptyData() {
        coEvery {
            forecastDataSource.getForecast(
                LAT.toString(),
                LAN.toString()
            )
        } returns Response.success(FORECAST_API_RESPONSE.copy(list = listOf()))
    }

    private fun forecastDataSourceReturnsSuccessListWithFutureDays(result: ForecastApiResponse) {
        coEvery {
            forecastDataSource.getForecast(
                LAT.toString(),
                LAN.toString()
            )
        } returns Response.success(result)
    }

    private suspend fun forecastDataSourceReturnsSuccess() {
        coEvery {
            forecastDataSource.getForecast(
                LAT.toString(),
                LAN.toString()
            )
        } returns Response.success(FORECAST_API_RESPONSE)
    }

    private suspend fun weatherDataSourceReturnsSuccess() {
        coEvery {
            weatherDataSource.getWeather(
                LAT.toString(),
                LAN.toString()
            )
        } returns Response.success(WEATHER_API_RESPONSE)
    }
}