package com.example.weather_radar_app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weather_radar_app.data.db.ListToStringConverter
import com.google.gson.annotations.SerializedName

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
@TypeConverters(ListToStringConverter::class)
data class CurrentWeatherEntry(
    val feelslike: Double,

    @SerializedName("is_day")
    val isDay: String,

    @SerializedName("observation_time")
    val observationTime: String,

    val precip: Double, // уровень осадков

    val pressure: Double, // атмосферное давление

    val temperature: Double,

    @SerializedName("uv_index")
    val uvIndex: Int,

    val visibility: Double,

    @SerializedName("weather_descriptions")
    val condition: List<String>,

    @SerializedName("weather_icons")
    val icon: List<String>,

    @SerializedName("wind_dir")
    val windDir: String,

    @SerializedName("wind_speed")
    val windSpeed: Double
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
}