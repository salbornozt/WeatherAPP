package com.satdev.weatherapp.core.api

import com.satdev.weatherapp.BuildConfig
import com.satdev.weatherapp.R
import com.satdev.weatherapp.core.domain.repository.StringRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AppInterceptor @Inject constructor(private val stringRepository: StringRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val currentUrl = chain.request().url
        val newUrl = currentUrl.newBuilder().addQueryParameter("appid", BuildConfig.API_KEY)
            .addQueryParameter("units", stringRepository.getString(R.string.unit_parameter)).build()
        val currentRequest = chain.request().newBuilder()
        val newRequest = currentRequest.url(newUrl).build()
        return chain.proceed(newRequest)
    }
}