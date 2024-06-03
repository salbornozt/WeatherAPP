package com.satdev.weatherapp.utils

object StringFormats {
    fun Double.formatTemperature(): String {
        return String.format("%.0f°", this)
    }

    fun Double.formatWindSpeed(unit: String = "mph"): String {
        return String.format("%.0f %s", this, unit)
    }
}