package com.satdev.weatherapp.core.data.repository

import android.content.res.Resources
import com.satdev.weatherapp.core.domain.repository.StringRepository

class AppStringRepository constructor(private val resources : Resources) : StringRepository {
    override fun getString(id: Int): String {
        return resources.getString(id)
    }
}