package com.example.weather_radar_app.data.network.response

import com.example.weather_radar_app.data.db.entity.FutureWeatherEntry
import com.example.weather_radar_app.data.db.entity.FutureWeatherLocation
import com.google.gson.annotations.SerializedName

data class FutureWeatherResponse(
    @SerializedName("daily")
    val futureWeatherEntries: List<FutureWeatherEntry>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int
) {
    val futureWeatherLocation: FutureWeatherLocation
        get() = FutureWeatherLocation(lat, lon, timezone, timezoneOffset)
}