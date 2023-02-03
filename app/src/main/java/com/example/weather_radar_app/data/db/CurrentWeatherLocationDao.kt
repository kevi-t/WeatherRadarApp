package com.example.weather_radar_app.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather_radar_app.data.db.entity.WEATHER_LOCATION_ID
import com.example.weather_radar_app.data.db.entity.CurrentWeatherLocation

/**
 * Created by NKazakova on 01.07.2020.
 */
@Dao
interface CurrentWeatherLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(currentWeatherLocation: CurrentWeatherLocation)

    @Query("select * from weather_location where id = $WEATHER_LOCATION_ID")
    fun getLocation(): LiveData<CurrentWeatherLocation>

    @Query("select * from weather_location where id = $WEATHER_LOCATION_ID")
    fun getLocationNonLive(): CurrentWeatherLocation?
}