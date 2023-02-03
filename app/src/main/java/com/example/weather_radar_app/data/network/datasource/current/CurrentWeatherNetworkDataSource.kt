package com.example.weather_radar_app.data.network.datasource.current

import androidx.lifecycle.LiveData
import com.example.weather_radar_app.data.network.response.CurrentWeatherResponse


interface CurrentWeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        units: String
    )
}