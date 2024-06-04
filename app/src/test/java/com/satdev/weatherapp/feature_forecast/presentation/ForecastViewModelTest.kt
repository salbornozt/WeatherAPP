package com.satdev.weatherapp.feature_forecast.presentation

import com.google.common.truth.Truth
import com.satdev.weatherapp.R
import com.satdev.weatherapp.core.domain.repository.LocationRepository
import com.satdev.weatherapp.core.domain.repository.StringRepository
import com.satdev.weatherapp.core.wrapper.ApiResult
import com.satdev.weatherapp.core.wrapper.ErrorWrapper
import com.satdev.weatherapp.feature_forecast.domain.ForecastRepository
import com.satdev.weatherapp.feature_forecast.domain.model.ForecastItemModel
import com.satdev.weatherapp.feature_home.domain.model.WeatherWindModel
import com.satdev.weatherapp.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date

class ForecastViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    lateinit var SUT: ForecastViewModel
    lateinit var locationRepository: LocationRepository
    lateinit var forecastRepository: ForecastRepository
    lateinit var stringRepository: StringRepository

    companion object {
        const val LAT = 0.0
        const val LAN = 0.0
        private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val FORECAST_ITEM_MODEL = ForecastItemModel(
            date = simpleDateFormat.format(Date()),
            highTemp = 60.0,
            lowTemp = 60.0,
            windModel = WeatherWindModel(
                speed = 60.0,
                deg = 160
            ),
            weatherDescription = "Cloudy",
            iconId = "a43"
        )
        private val LOCATION_ERROR = "Error Getting Device Location"
        private val SERVICE_ERROR = "We encountered an issue while fetching forecast information. Please try again in a few moments."
    }

    @Test
    fun `get View State shows screen data after permission granted`() = runTest {
        locationRepository = mockk()
        forecastRepository = mockk()
        stringRepository = mockk()
        locationRepositoryReturnsOk()
        forecastRepositoryReturnsSuccess()
        SUT = ForecastViewModel(locationRepository, forecastRepository, stringRepository)
        val results = mutableListOf<ForecastViewState>()
        val job = launch {
            SUT.viewState.toList(results)
        }
        SUT.initialize()
        // GIVEN I have accepted location permission
        SUT.onPermissionGranted()
        //WHEN I am on the forecast screen
        runCurrent()
        //THEN I will see the five-day forecast per the UX details below
        Truth.assertThat(results.last()).isInstanceOf(ForecastViewState.Success::class.java)
        Truth.assertThat((results.last() as ForecastViewState.Success).forecastData)
            .isEqualTo(listOf(FORECAST_ITEM_MODEL))
        job.cancel()
    }

    @Test
    fun `get View State shows error screen after getting location error`() = runTest {
        locationRepository = mockk()
        forecastRepository = mockk()
        stringRepository = mockk()
        locationRepositoryReturnsError()
        forecastRepositoryReturnsSuccess()
        initStringRepo()
        SUT = ForecastViewModel(locationRepository, forecastRepository, stringRepository)
        val results = mutableListOf<ForecastViewState>()
        val job = launch {
            SUT.viewState.toList(results)
        }

        // GIVEN I have accepted location permission
        SUT.initialize()
        //WHEN I am on the forecast screen and an error occurred getting device location
        runCurrent()
        //THEN I will see the error screen
        Truth.assertThat(results.last()).isInstanceOf(ForecastViewState.Error::class.java)
        Truth.assertThat((results.last() as ForecastViewState.Error).error)
            .isEqualTo(LOCATION_ERROR)
        job.cancel()
    }

    @Test
    fun `get View State shows error screen after getting forecast service error`() = runTest {
        locationRepository = mockk()
        forecastRepository = mockk()
        stringRepository = mockk()
        locationRepositoryReturnsOk()
        forecastRepositoryReturnsError()
        initStringRepo()
        SUT = ForecastViewModel(locationRepository, forecastRepository, stringRepository)
        val results = mutableListOf<ForecastViewState>()
        val job = launch {
            SUT.viewState.toList(results)
        }

        // GIVEN I have accepted location permission
        SUT.initialize()
        //WHEN I am on the forecast screen an error occurred getting server data
        runCurrent()
        //THEN I will see the error screen
        Truth.assertThat(results.last()).isInstanceOf(ForecastViewState.Error::class.java)
        Truth.assertThat((results.last() as ForecastViewState.Error).error)
            .isEqualTo(SERVICE_ERROR)
        job.cancel()
    }

    @Test
    fun `on Refresh shows hide loader`() = runTest {
        locationRepository = mockk()
        forecastRepository = mockk()
        stringRepository = mockk()
        locationRepositoryReturnsOk()
        forecastRepositoryReturnsSuccess()
        SUT = ForecastViewModel(locationRepository, forecastRepository, stringRepository)
        val results = mutableListOf<Boolean>()
        val job = launch {
            SUT.refreshState.toList(results)
        }
        // GIVEN I am viewing the landing screen
        SUT.initialize()

        //WHEN I want to refresh data
        SUT.onRefresh()

        runCurrent()
        //THEN I can pull down to refresh the data
        Truth.assertThat(results).isEqualTo(listOf(false))

        job.cancel()
    }

    private fun forecastRepositoryReturnsError() {
        coEvery { forecastRepository.getForecastList(
            LAT.toString(),
            LAN.toString()
        ) } returns ApiResult.Error(ErrorWrapper.UnknownError)
    }

    private fun initStringRepo() {
        coEvery { stringRepository.getString(R.string.error_getting_locations) } returns LOCATION_ERROR
        coEvery { stringRepository.getString(R.string.error_fetching_forecast) } returns SERVICE_ERROR
    }

    private fun locationRepositoryReturnsError() {
        coEvery { locationRepository.getCurrentLocation() } returns ApiResult.Error(ErrorWrapper.ErrorGettingLocation)
    }

    private fun forecastRepositoryReturnsSuccess() {
        coEvery {
            forecastRepository.getForecastList(
                LAT.toString(),
                LAN.toString()
            )
        } returns ApiResult.Success(listOf(FORECAST_ITEM_MODEL))
    }

    private fun locationRepositoryReturnsOk() {
        coEvery { locationRepository.getCurrentLocation() } returns ApiResult.Success(
            Pair(
                LAT,
                LAN
            )
        )
    }
}