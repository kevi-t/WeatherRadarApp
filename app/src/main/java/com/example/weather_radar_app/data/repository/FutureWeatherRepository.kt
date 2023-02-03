package com.example.weather_radar_app.data.repository

import androidx.lifecycle.LiveData
import com.example.weather_radar_app.data.db.entity.FutureWeatherEntry
import com.example.weather_radar_app.data.db.entity.FutureWeatherLocation
import org.threeten.bp.LocalDate


interface FutureWeatherRepository {
    suspend fun getFutureWeatherList(startDate: LocalDate, unitSystem: String): LiveData<out List<FutureWeatherEntry>>

    suspend fun getFutureWeatherLocation(): LiveData<FutureWeatherLocation>

    suspend fun getFutureWeatherByDate(date: Long, unitSystem: String): LiveData<out FutureWeatherEntry>
}