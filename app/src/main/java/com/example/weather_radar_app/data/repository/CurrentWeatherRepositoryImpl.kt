package com.example.weather_radar_app.data.repository

import androidx.lifecycle.LiveData
import com.example.weather_radar_app.data.db.CurrentWeatherDao
import com.example.weather_radar_app.data.db.CurrentWeatherLocationDao
import com.example.weather_radar_app.data.db.entity.CurrentWeatherEntry
import com.example.weather_radar_app.data.db.entity.CurrentWeatherLocation
import com.example.weather_radar_app.data.network.datasource.current.CurrentWeatherNetworkDataSource
import com.example.weather_radar_app.data.network.response.CurrentWeatherResponse
import com.example.weather_radar_app.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime


class CurrentWeatherRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val currentWeatherLocationDao: CurrentWeatherLocationDao,
    private val dataSource: CurrentWeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : CurrentWeatherRepository {

    init {
        dataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(unitSystem: String): LiveData<out CurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData(unitSystem)
            currentWeatherDao.getWeather()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<CurrentWeatherLocation> {
        return withContext(Dispatchers.IO) {
            currentWeatherLocationDao.getLocation()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
            currentWeatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private suspend fun initWeatherData(unitSystem: String) {
        val lastWeatherLocation = currentWeatherLocationDao.getLocationNonLive()

        if (lastWeatherLocation == null
            || locationProvider.hasLocationChanged(lastWeatherLocation)
        ) {
            fetchCurrentWeather(unitSystem)
            return
        }

        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather(unitSystem)
    }

    private suspend fun fetchCurrentWeather(unitSystem: String) {
        dataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString(), unitSystem
        )
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}