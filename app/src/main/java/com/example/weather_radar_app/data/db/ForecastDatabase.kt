package com.example.weather_radar_app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weather_radar_app.data.db.entity.CurrentWeatherEntry
import com.example.weather_radar_app.data.db.entity.CurrentWeatherLocation
import com.example.weather_radar_app.data.db.entity.FutureWeatherEntry
import com.example.weather_radar_app.data.db.entity.FutureWeatherLocation


@Database(
    entities = [CurrentWeatherEntry::class, FutureWeatherEntry::class, CurrentWeatherLocation::class, FutureWeatherLocation::class],
    version = 1
)

abstract class ForecastDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun futureWeatherDao(): FutureWeatherDao
    abstract fun currentWeatherLocationDao(): CurrentWeatherLocationDao
    abstract fun futureWeatherLocationDao(): FutureWeatherLocationDao
}