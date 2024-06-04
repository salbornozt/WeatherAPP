package com.satdev.weatherapp.core.domain.repository

import androidx.annotation.StringRes

interface StringRepository {
    fun getString(@StringRes id : Int) : String

}