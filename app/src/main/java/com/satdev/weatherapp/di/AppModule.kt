package com.satdev.weatherapp.di

import com.satdev.weatherapp.core.api.ApiService
import com.satdev.weatherapp.core.api.AppInterceptor
import com.satdev.weatherapp.core.api.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesRetrofitClient(): OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(300, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .addInterceptor(AppInterceptor()).build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(retrofitClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(retrofitClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }
}