package com.satdev.weatherapp.di

import com.satdev.weatherapp.core.api.ApiService
import com.satdev.weatherapp.core.data.repository.dataSource.ForecastDataSource
import com.satdev.weatherapp.core.data.repository.dataSourceImpl.RemoteForecastDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ForecastModule {
    @Singleton
    @Provides
    fun providesForecastDataSource(apiService: ApiService) : ForecastDataSource {
        return RemoteForecastDataSourceImpl(apiService)
    }
}