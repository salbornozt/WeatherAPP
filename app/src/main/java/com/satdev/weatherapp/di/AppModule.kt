package com.satdev.weatherapp.di

import android.content.Context
import com.satdev.weatherapp.core.api.ApiService
import com.satdev.weatherapp.core.api.AppInterceptor
import com.satdev.weatherapp.core.api.BASE_URL
import com.satdev.weatherapp.core.data.repository.AppStringRepository
import com.satdev.weatherapp.core.domain.repository.StringRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun providesRetrofitClient(stringRepository: StringRepository): OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(300, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .addInterceptor(AppInterceptor(stringRepository)).build()
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

    @Singleton
    @Provides
    fun providesStringRepository(@ApplicationContext context: Context) : StringRepository{
        return AppStringRepository(context.resources)
    }
}