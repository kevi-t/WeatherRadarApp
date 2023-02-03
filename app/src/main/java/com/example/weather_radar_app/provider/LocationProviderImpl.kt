package com.example.weather_radar_app.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.weather_radar_app.data.db.entity.CurrentWeatherLocation
import com.example.weather_radar_app.data.db.entity.WeatherLocation
import com.example.weather_radar_app.internal.LocationPermissionNotGrantedException
import com.example.weather_radar_app.internal.asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : PreferenceProvider(context),
    LocationProvider {

    private val appContext = context.applicationContext

    override suspend fun hasLocationChanged(location: WeatherLocation): Boolean {
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(location)
        } catch (e: LocationPermissionNotGrantedException) {
            false
        }
        return deviceLocationChanged || hasCustomLocationChanged(location as? CurrentWeatherLocation)
    }

    override suspend fun getPreferredLocationString(): String {
        if (isUsingDeviceLocation()) {
            try {
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return "${getCustomLocationName()}"
                return "${deviceLocation.latitude},${deviceLocation.longitude}"
            } catch (e: LocationPermissionNotGrantedException) {
                return "${getCustomLocationName()}"
            }
        } else
            return "${getCustomLocationName()}"
    }

    private suspend fun hasDeviceLocationChanged(location: WeatherLocation): Boolean {
        if (!isUsingDeviceLocation()) {
            return false
        }

        val deviceLocation = getLastDeviceLocation().await()
            ?: return false

        val comparisonThreshold = 0.03
        return Math.abs(deviceLocation.latitude - location.lat) > comparisonThreshold &&
                Math.abs(deviceLocation.longitude - location.lon) > comparisonThreshold
    }

    private fun hasCustomLocationChanged(location: CurrentWeatherLocation?): Boolean {
        if (location == null) return false

        if (!isUsingDeviceLocation()) {
            val customLocation = getCustomLocationName()
            return customLocation != location.name
        }
        return false
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        return if (hasLocationPermissions()) fusedLocationProviderClient.lastLocation.asDeferred()
        else throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(appContext,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}