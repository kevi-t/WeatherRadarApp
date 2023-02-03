package com.example.weather_radar_app.fragments

import androidx.lifecycle.ViewModel
import com.example.weather_radar_app.data.repository.FutureWeatherRepository
import com.example.weather_radar_app.internal.UnitSystem
import com.example.weather_radar_app.internal.lazyDeferred
import com.example.weather_radar_app.provider.UnitProvider
import org.threeten.bp.LocalDate
import java.util.*

class FutureViewModel(repository: FutureWeatherRepository, unitProvider: UnitProvider) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weatherEntries by lazyDeferred {
        repository.getFutureWeatherList(LocalDate.now(),
            unitSystem.name.lowercase(Locale.getDefault())
        )
    }

    val weatherLocation by lazyDeferred {
        repository.getFutureWeatherLocation()
    }
}