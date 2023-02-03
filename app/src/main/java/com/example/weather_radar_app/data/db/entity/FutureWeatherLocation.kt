package com.example.weather_radar_app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime


const val FUTURE_WEATHER_LOCATION_ID = 0

@Entity(tableName = "future_weather_location")
data class FutureWeatherLocation(
    override val lat: Double,
    override val lon: Double,
    val timezoneId: String,
    val timezoneOffset: Int
) : WeatherLocation {
    @PrimaryKey(autoGenerate = false)
    var id: Int = FUTURE_WEATHER_LOCATION_ID

    override val zonedDateTime: ZonedDateTime
        get() {
            val dateTime = LocalDateTime.now(ZoneId.of(timezoneId))
            val zoneId = ZoneId.of(timezoneId)
            val zoneOffset = ZoneOffset.ofTotalSeconds(timezoneOffset)
            return ZonedDateTime.ofInstant(dateTime, zoneOffset, zoneId)
        }
}