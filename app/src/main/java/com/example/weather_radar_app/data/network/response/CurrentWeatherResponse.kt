package com.example.weather_radar_app.data.network.response

import com.example.weather_radar_app.data.db.entity.CurrentWeatherEntry
import com.example.weather_radar_app.data.db.entity.CurrentWeatherLocation
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    val location: CurrentWeatherLocation,
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry
)