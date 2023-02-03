package com.example.weather_radar_app.data.network.datasource.current

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather_radar_app.data.network.WeatherStackApiService
import com.example.weather_radar_app.data.network.response.CurrentWeatherResponse
import com.example.weather_radar_app.internal.NoConnectivityException


class CurrentWeatherNetworkDataSourceImpl(
    private val weatherStackApiService: WeatherStackApiService
) : CurrentWeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String, units: String) {
        try {
            val fetchedCurrentWeather =
                weatherStackApiService.getCurrentWeather(location, units)
                    .await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}