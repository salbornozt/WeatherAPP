package com.satdev.weatherapp.core.domain.repository

import com.satdev.weatherapp.core.wrapper.ApiResult

interface LocationRepository {
    suspend fun getCurrentLocation() : ApiResult<Pair<Double,Double>>
}