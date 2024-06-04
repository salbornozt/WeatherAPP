package com.satdev.weatherapp.core.data.repository.dataSourceImpl

import com.google.common.truth.Truth
import com.satdev.weatherapp.core.api.ApiService
import com.satdev.weatherapp.core.data.model.Clouds
import com.satdev.weatherapp.core.data.model.Coord
import com.satdev.weatherapp.core.data.model.Weather
import com.satdev.weatherapp.core.data.model.Wind
import com.satdev.weatherapp.feature_forecast.data.model.City
import com.satdev.weatherapp.feature_forecast.data.model.ForecastApiResponse
import com.satdev.weatherapp.feature_forecast.data.model.ForecastItem
import com.satdev.weatherapp.feature_forecast.data.model.ForecastSys
import com.satdev.weatherapp.feature_forecast.data.model.MainForecast
import com.satdev.weatherapp.feature_forecast.data.model.Rain
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

class RemoteForecastDataSourceImplTest {
    private lateinit var apiService: ApiService
    private lateinit var SUT : RemoteForecastDataSourceImpl
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
                        lat = LAT,
                        lon = LAN
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

        val FORECAST_SERVER_ERROR : Response<ForecastApiResponse> = Response.error(500,"".toResponseBody())
    }
    @Before
    fun setUp() {
        apiService = mockk()
        SUT = RemoteForecastDataSourceImpl(apiService)
    }

    @Test
    fun `get Forecast returns success`() = runBlocking {
        //Arrange
        apiServicesReturnsSuccess()
        //Act
        val result = SUT.getForecast(LAT.toString(), LAN.toString())
        //Assert
        Truth.assertThat(result.isSuccessful).isEqualTo(true)
        Truth.assertThat(result.body())
            .isEqualTo(FORECAST_API_RESPONSE)
        coVerify(exactly = 1) { apiService.getForecast(LAT.toString(), LAN.toString()) }
    }

    @Test
    fun `get Forecast returns error`() = runBlocking {
        //Arrange
        apiServicesReturnsError()
        //Act
        val result = SUT.getForecast(LAT.toString(), LAN.toString())
        //Assert
        Truth.assertThat(result.isSuccessful).isEqualTo(false)
        coVerify(exactly = 1) { apiService.getForecast(LAT.toString(), LAN.toString()) }
    }

    private fun apiServicesReturnsError() {
        coEvery { apiService.getForecast(LAT.toString(), LAN.toString()) } returns FORECAST_SERVER_ERROR
    }


    private fun apiServicesReturnsSuccess() {
        coEvery { apiService.getForecast(LAT.toString(), LAN.toString()) } returns Response.success(
            FORECAST_API_RESPONSE
        )
    }
}