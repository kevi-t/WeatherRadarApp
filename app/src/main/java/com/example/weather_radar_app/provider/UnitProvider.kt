package com.example.weather_radar_app.provider

import com.example.weather_radar_app.internal.UnitSystem


interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}