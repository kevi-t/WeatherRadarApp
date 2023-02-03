package com.example.weather_radar_app.data.db

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate
import com.example.weather_radar_app.data.db.entity.Weather

/**
 * Created by NKazakova on 30.06.2020.
 */
object ListToStringConverter {
    @TypeConverter
    @JvmStatic
    fun fromList(list: List<String>): String = list.joinToString(separator = ",")

    @TypeConverter
    @JvmStatic
    fun toList(data: String): List<String> = data.split(",")
}

object LocalDateToLongConverter {
    @TypeConverter
    @JvmStatic
    fun longToDate(long: Long?) = long?.let {
        LocalDate.ofEpochDay(long)
    }

    @TypeConverter
    @JvmStatic
    fun dateToLong(dateTime: LocalDate?) = dateTime?.toEpochDay()
}

object WeatherListToSingleItemConverter {
    @TypeConverter
    @JvmStatic
    fun weatherListToWeather(weatherList: List<Weather>?): Weather? = weatherList?.get(0)

    @TypeConverter
    @JvmStatic
    fun weatherToWeatherList(weather: Weather?): List<Weather?> = listOf(weather)
}