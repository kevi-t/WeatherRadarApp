package com.example.weather_radar_app.data.network.datasource.future

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather_radar_app.data.network.OpenWeatherMapApiService
import com.example.weather_radar_app.data.network.response.FutureWeatherResponse
import com.example.weather_radar_app.internal.NoConnectivityException


const val FORECAST_DAYS_COUNT = 7

class FutureWeatherNetworkDataSourceImpl(
    private val openWeatherMapApiService: OpenWeatherMapApiService
) : FutureWeatherNetworkDataSource {

    private val _downloadedFutureWeather = MutableLiveData<FutureWeatherResponse>()
    override val downloadedFutureWeather: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureWeather

    override suspend fun fetchFutureWeather(lat: Double, lon: Double, units: String) {
        try {
            val fetchedFutureWeather =
                openWeatherMapApiService.getFutureWeather(lat, lon, units)
                    .await()
            _downloadedFutureWeather.postValue(fetchedFutureWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}