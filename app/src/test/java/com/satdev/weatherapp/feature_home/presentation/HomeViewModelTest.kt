package com.satdev.weatherapp.feature_home.presentation


import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.satdev.weatherapp.R
import com.satdev.weatherapp.core.domain.repository.LocationRepository
import com.satdev.weatherapp.core.domain.repository.StringRepository
import com.satdev.weatherapp.core.wrapper.ApiResult
import com.satdev.weatherapp.core.wrapper.ErrorWrapper
import com.satdev.weatherapp.feature_home.domain.WeatherRepository
import com.satdev.weatherapp.feature_home.domain.model.HomeModel
import com.satdev.weatherapp.feature_home.domain.model.WeatherItemModel
import com.satdev.weatherapp.feature_home.domain.model.WeatherModel
import com.satdev.weatherapp.feature_home.domain.model.WeatherWindModel
import com.satdev.weatherapp.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date

class HomeViewModelTest {
    private lateinit var SUT : HomeViewModel
    private lateinit var locationRepository: LocationRepository
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var stringRepository: StringRepository
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    companion object {
        const val LAT = 0.0
        const val LAN = 0.0
        private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val ACTUAL_WEATHER = WeatherModel(
            cityName = "Bogota",
            mainWeather = "Cloudy",
            feelsLike = 60.0,
            highTemp = 60.0,
            lowTemp = 60.0,
            windModel = WeatherWindModel(
                speed = 60.0,
                deg = 165
            )
        )
        val WEATHER_ITEM_MODEL = WeatherItemModel(
            date = simpleDateFormat.format(Date()),
            temperature = 60.0,
            iconId = "30s"
        )
        val NEXT_WEATHER = listOf(
            WEATHER_ITEM_MODEL, WEATHER_ITEM_MODEL
        )
        val HOME_WEATHER_RESULT = HomeModel(ACTUAL_WEATHER, NEXT_WEATHER)
        private val LOCATION_ERROR = "Error Getting Device Location"
        private val SERVICE_ERROR = "We encountered an issue while fetching weather information. Please try again in a few moments."
    }
    @Before
    fun setUp() {

    }

    @Test
    fun `get View State shows location permission`() = runTest{
        // Given I have not accepted location permission
        locationRepository = mockk()
        weatherRepository = mockk()
        stringRepository = mockk()
        locationRepositoryReturnsNoPermission()
        SUT = HomeViewModel(locationRepository, weatherRepository, stringRepository)
        // When I view the landing screen
        val results = mutableListOf<HomeViewState>()
        val job = launch {
            SUT.viewState.toList(results)
        }
        SUT.initialize()
        runCurrent()
        // THEN I am prompted to provide location permission
        assertThat(results.last()).isEqualTo(HomeViewState.RequestPermission)
        job.cancel()
    }

    @Test
    fun `get View State shows screen data after permission granted`() = runTest{
        locationRepository = mockk()
        weatherRepository = mockk()
        stringRepository = mockk()
        locationRepositoryReturnsOk()
        weatherRepositoryReturnsSuccess()
        SUT = HomeViewModel(locationRepository, weatherRepository, stringRepository)
        val results = mutableListOf<HomeViewState>()
        val job = launch {
            SUT.viewState.toList(results)
        }
        SUT.initialize()
        // Given I have accepted location permission
        SUT.onPermissionGranted()
        runCurrent()
        // When I view the landing screen
        // THEN I will see my current day’s weather information AND I will see 3-hour increments on weather for
        //that day AND the high and low temps will be shown for the day per the UX details below
        assertThat(results.last()).isInstanceOf(HomeViewState.Success::class.java)
        assertThat((results.last() as HomeViewState.Success).weatherData).isEqualTo(HOME_WEATHER_RESULT)
        job.cancel()
    }

    @Test
    fun `get View State shows error screen after getting location error`() = runTest {
        locationRepository = mockk()
        weatherRepository = mockk()
        stringRepository = mockk()
        locationRepositoryReturnsError()
        weatherRepositoryReturnsSuccess()
        initStringRepo()
        SUT = HomeViewModel(locationRepository, weatherRepository, stringRepository)
        val results = mutableListOf<HomeViewState>()
        val job = launch {
            SUT.viewState.toList(results)
        }

        // GIVEN I have accepted location permission
        SUT.initialize()
        //WHEN I am on the landing screen and an error occurred getting device location
        runCurrent()
        //THEN I will see the error screen
        Truth.assertThat(results.last()).isInstanceOf(HomeViewState.Error::class.java)
        Truth.assertThat((results.last() as HomeViewState.Error).message)
            .isEqualTo(LOCATION_ERROR)
        job.cancel()
    }

    @Test
    fun `get View State shows error screen after getting weather service error`() = runTest {
        locationRepository = mockk()
        weatherRepository = mockk()
        stringRepository = mockk()
        locationRepositoryReturnsOk()
        weatherRepositoryReturnsError()
        initStringRepo()
        SUT = HomeViewModel(locationRepository, weatherRepository, stringRepository)
        val results = mutableListOf<HomeViewState>()
        val job = launch {
            SUT.viewState.toList(results)
        }

        // GIVEN I have accepted location permission
        SUT.initialize()
        //WHEN I am on the landing screen and an error occurred getting device location
        runCurrent()
        //THEN I will see the error screen
        Truth.assertThat(results.last()).isInstanceOf(HomeViewState.Error::class.java)
        Truth.assertThat((results.last() as HomeViewState.Error).message)
            .isEqualTo(SERVICE_ERROR)
        job.cancel()
    }



    @Test
    fun `on Refresh shows hide loader`() = runTest {
        locationRepository = mockk()
        weatherRepository = mockk()
        stringRepository = mockk()
        locationRepositoryReturnsOk()
        weatherRepositoryReturnsSuccess()
        SUT = HomeViewModel(locationRepository, weatherRepository, stringRepository)
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

    private fun locationRepositoryReturnsError() {
        coEvery { locationRepository.getCurrentLocation() } returns ApiResult.Error(ErrorWrapper.ErrorGettingLocation)
    }

    private fun initStringRepo() {
        coEvery { stringRepository.getString(R.string.error_getting_locations) } returns LOCATION_ERROR
        coEvery { stringRepository.getString(R.string.error_fetching_weather) } returns SERVICE_ERROR
    }

    private fun weatherRepositoryReturnsError() {
        coEvery { weatherRepository.getWeather(LAT.toString(), LAN.toString()) } returns ApiResult.Error(ErrorWrapper.UnknownError)
    }

    private fun weatherRepositoryReturnsSuccess() {
        coEvery { weatherRepository.getWeather(LAT.toString(), LAN.toString()) } returns ApiResult.Success(HOME_WEATHER_RESULT)
    }

    private fun locationRepositoryReturnsNoPermission() {
        coEvery { locationRepository.getCurrentLocation() } returns ApiResult.Error(ErrorWrapper.NoLocationPermission)
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