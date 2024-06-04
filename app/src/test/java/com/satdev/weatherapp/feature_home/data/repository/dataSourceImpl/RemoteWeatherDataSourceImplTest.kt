package com.satdev.weatherapp.feature_home.data.repository.dataSourceImpl

import com.google.common.truth.Truth.assertThat
import com.satdev.weatherapp.core.api.ApiService
import com.satdev.weatherapp.core.data.model.Clouds
import com.satdev.weatherapp.core.data.model.Coord
import com.satdev.weatherapp.core.data.model.Weather
import com.satdev.weatherapp.core.data.model.Wind
import com.satdev.weatherapp.feature_home.data.model.Main
import com.satdev.weatherapp.feature_home.data.model.Sys
import com.satdev.weatherapp.feature_home.data.model.WeatherApiResponse
import com.satdev.weatherapp.feature_home.data.repository.WeatherRepositoryImplTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RemoteWeatherDataSourceImplTest {
    private lateinit var apiService: ApiService
    private lateinit var SUT : RemoteWeatherDataSourceImpl
    companion object {
        const val LAT = 0.0
        const val LAN = 0.0
        val WEATHER_API_RESPONSE = WeatherApiResponse(
            base = "stations",
            clouds = Clouds(all = 20),
            cod = 200,
            coord = Coord(lat = WeatherRepositoryImplTest.LAT, WeatherRepositoryImplTest.LAN),
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
        val WEATHER_SERVER_ERROR = Response.error<WeatherApiResponse>(500,"".toResponseBody())
    }
    @Before
    fun setUp() {
        apiService = mockk()
        SUT = RemoteWeatherDataSourceImpl(apiService)
    }

    @Test
    fun `get Weather returns success`() = runBlocking {
        //Arrange
        apiServicesReturnsSuccess()
        //Act
        val result = SUT.getWeather(LAT.toString(), LAN.toString())
        //Assert
        assertThat(result.isSuccessful).isEqualTo(true)
        assertThat(result.body()).isEqualTo(WEATHER_API_RESPONSE)
        coVerify(exactly = 1) { apiService.getWeather(LAT.toString(), LAN.toString()) }
    }

    @Test
    fun `get Weather returns error`() = runBlocking {
        //Arrange
        apiServicesReturnsError()
        //Act
        val result = SUT.getWeather(LAT.toString(), LAN.toString())
        //Assert
        assertThat(result.isSuccessful).isEqualTo(false)
        coVerify(exactly = 1) { apiService.getWeather(LAT.toString(), LAN.toString()) }
    }

    private fun apiServicesReturnsError() {
        coEvery { apiService.getWeather(LAT.toString(), LAN.toString()) } returns WEATHER_SERVER_ERROR
    }

    private fun apiServicesReturnsSuccess() {
        coEvery { apiService.getWeather(LAT.toString(), LAN.toString()) } returns Response.success(WEATHER_API_RESPONSE)

    }
}