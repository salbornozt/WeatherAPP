package com.satdev.weatherapp.core.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.satdev.weatherapp.core.domain.repository.LocationRepository
import com.satdev.weatherapp.core.wrapper.ApiResult
import com.satdev.weatherapp.core.wrapper.ErrorWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class LocationRepositoryImpl constructor(
    private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationRepository {

    override suspend fun getCurrentLocation(): ApiResult<Pair<Double, Double>> = withContext(Dispatchers.IO) {
        if (!hasLocationPermission()) {
            return@withContext ApiResult.Error(ErrorWrapper.NoLocationPermission)
        }
        return@withContext getDeviceFusedLocation()
    }

    @SuppressLint("MissingPermission")
    private suspend fun getDeviceFusedLocation() : ApiResult<Pair<Double,Double>> = suspendCancellableCoroutine {continuation->
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                // If location is not null, invoke the success callback with latitude and longitude
                continuation.resume(ApiResult.Success(Pair(it.latitude, it.longitude)))
            }
        }.addOnFailureListener { _ ->
            // If an error occurs, invoke the failure callback with the exception
            val result : ApiResult<Pair<Double,Double>> = ApiResult.Error(ErrorWrapper.ErrorGettingLocation)
            continuation.resume(result)
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}