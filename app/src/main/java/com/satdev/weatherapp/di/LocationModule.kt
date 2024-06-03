package com.satdev.weatherapp.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.satdev.weatherapp.core.data.repository.LocationRepositoryImpl
import com.satdev.weatherapp.core.domain.repository.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Singleton
    @Provides
    fun providesFusedLocationClient(@ApplicationContext context: Context) : FusedLocationProviderClient{
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Singleton
    @Provides
    fun providesLocationRepository(@ApplicationContext context: Context, fusedLocationProviderClient: FusedLocationProviderClient) : LocationRepository {
        return LocationRepositoryImpl(context, fusedLocationProviderClient)
    }
}