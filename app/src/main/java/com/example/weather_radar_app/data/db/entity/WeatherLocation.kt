package com.example.weather_radar_app.data.db.entity

import org.threeten.bp.ZonedDateTime

interface WeatherLocation {
    val lat: Double
    val lon: Double
    val zonedDateTime: ZonedDateTime
}