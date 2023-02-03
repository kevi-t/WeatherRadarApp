package com.example.weather_radar_app.fragments

import androidx.lifecycle.ViewModel
import com.example.weather_radar_app.data.repository.FutureWeatherRepository
import com.example.weather_radar_app.internal.UnitSystem
import com.example.weather_radar_app.internal.lazyDeferred
import com.example.weather_radar_app.provider.UnitProvider
import java.util.*

class WeeklyViewModel(private val detailDate: Long, private val repository: FutureWeatherRepository, unitProvider: UnitProvider) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        repository.getFutureWeatherByDate(detailDate,
            unitSystem.name.lowercase(Locale.getDefault())
        )
    }

    val weatherLocation by lazyDeferred {
        repository.getFutureWeatherLocation()
    }
}