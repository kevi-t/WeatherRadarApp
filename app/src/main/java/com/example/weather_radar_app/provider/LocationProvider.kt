package com.example.weather_radar_app.provider

import com.example.weather_radar_app.data.db.entity.WeatherLocation

interface LocationProvider {
    suspend fun hasLocationChanged(location: WeatherLocation): Boolean
    suspend fun getPreferredLocationString(): String
}