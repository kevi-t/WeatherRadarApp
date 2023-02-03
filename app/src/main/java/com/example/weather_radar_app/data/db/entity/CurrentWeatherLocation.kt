package com.example.weather_radar_app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

const val WEATHER_LOCATION_ID = 0

@Entity(tableName = "weather_location")
data class CurrentWeatherLocation(
    val name: String,
    val region: String,
    val country: String,
    override val lat: Double,
    override val lon: Double,
    @SerializedName("timezone_id")
    val timezoneId: String,
    @SerializedName("localtime_epoch")
    val localtimeEpoch: Long,
    @SerializedName("utc_offset")
    val utcOffset: String,
    val localtime: String
) : WeatherLocation {
    @PrimaryKey(autoGenerate = false)
    var id: Int = WEATHER_LOCATION_ID

    override val zonedDateTime: ZonedDateTime
        get() {
            val instant = Instant.ofEpochSecond(localtimeEpoch)
            val zoneId = ZoneId.of(timezoneId)
            return ZonedDateTime.ofInstant(instant, zoneId)
        }
}