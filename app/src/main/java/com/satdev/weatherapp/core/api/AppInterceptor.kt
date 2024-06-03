package com.satdev.weatherapp.core.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AppInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val currentUrl = chain.request().url
        // TODO: quitar llave 
        val newUrl = currentUrl.newBuilder().addQueryParameter("appid", "dcb2f95189e962e1c51ceb08d64d40e6").build()
        val currentRequest = chain.request().newBuilder()
        val newRequest = currentRequest.url(newUrl).build()
        Log.d("sat_tag", "intercept: $newUrl $newRequest")
        return chain.proceed(newRequest)
    }
}