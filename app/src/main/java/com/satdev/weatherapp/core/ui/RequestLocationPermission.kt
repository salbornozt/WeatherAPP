package com.satdev.weatherapp.core.ui

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionsRevoked: () -> Unit,
    shouldShowRationale : () -> Unit,
) {
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    val allPermissionsRevoked =
        permissionState.permissions.size == permissionState.revokedPermissions.size

    val permissionsToRequest = permissionState.permissions.filter {
        !it.status.isGranted
    }

    if (permissionsToRequest.isNotEmpty()) {
        LaunchedEffect(permissionState) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    if (allPermissionsRevoked) {
        if (permissionState.shouldShowRationale){
            shouldShowRationale()
        } else {
            onPermissionsRevoked()
        }
    } else {
        if (permissionState.allPermissionsGranted) {
            onPermissionGranted()
        } else {

            onPermissionDenied()
        }
    }
}