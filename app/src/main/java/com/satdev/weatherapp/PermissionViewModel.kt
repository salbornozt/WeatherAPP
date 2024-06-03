package com.satdev.weatherapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PermissionViewModel : ViewModel(){
    private val _permissionState :MutableStateFlow<PermissionState> = MutableStateFlow(PermissionState.Unknown)
    val permissionState: StateFlow<PermissionState> = _permissionState.asStateFlow()

    fun onPermissionGranted() {
        _permissionState.value = PermissionState.Granted
    }

    fun onPermissionDenied() {
        _permissionState.value = PermissionState.Denied
    }

    fun onShouldShowRationale(){
        _permissionState.value = PermissionState.ShouldShowRationale
    }

}

sealed class PermissionState {
    object Unknown : PermissionState()
    object Granted : PermissionState()
    object Denied : PermissionState()

    object ShouldShowRationale : PermissionState()
}