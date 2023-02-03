package com.example.weather_radar_app.data.network.datasource.future

import androidx.lifecycle.LiveData
import com.example.weather_radar_app.data.network.response.FutureWeatherResponse


interface FutureWeatherNetworkDataSource {
    val downloadedFutureWeather: LiveData<FutureWeatherResponse>

    suspend fun fetchFutureWeather(
        lat: Double,
        lon: Double,
        units: String
    )
}