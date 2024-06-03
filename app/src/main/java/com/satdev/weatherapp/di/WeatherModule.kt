package com.satdev.weatherapp.di

import com.satdev.weatherapp.core.api.ApiService
import com.satdev.weatherapp.core.data.repository.dataSource.ForecastDataSource
import com.satdev.weatherapp.feature_home.data.repository.WeatherRepositoryImpl
import com.satdev.weatherapp.feature_home.data.repository.dataSource.WeatherDataSource
import com.satdev.weatherapp.feature_home.data.repository.dataSourceImpl.RemoteWeatherDataSourceImpl
import com.satdev.weatherapp.feature_home.domain.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {
    @Singleton
    @Provides
    fun providesWeatherDataSource(apiService: ApiService) : WeatherDataSource {
        return RemoteWeatherDataSourceImpl(apiService)
    }
    @Singleton
    @Provides
    fun providesWeatherRepository(weatherDataSource: WeatherDataSource, forecastDataSource: ForecastDataSource) : WeatherRepository {
        return WeatherRepositoryImpl(weatherDataSource = weatherDataSource, forecastDataSource)
    }
}