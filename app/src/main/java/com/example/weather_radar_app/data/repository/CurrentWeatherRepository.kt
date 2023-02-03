package com.example.weather_radar_app.data.repository

import androidx.lifecycle.LiveData
import com.example.weather_radar_app.data.db.entity.CurrentWeatherEntry
import com.example.weather_radar_app.data.db.entity.CurrentWeatherLocation

interface CurrentWeatherRepository {
    suspend fun getCurrentWeather(unitSystem: String): LiveData<out CurrentWeatherEntry>

    suspend fun getWeatherLocation(): LiveData<CurrentWeatherLocation>
}