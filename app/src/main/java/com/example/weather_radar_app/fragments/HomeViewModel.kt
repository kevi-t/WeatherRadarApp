package com.example.weather_radar_app.fragments

import androidx.lifecycle.ViewModel
import com.example.weather_radar_app.data.repository.CurrentWeatherRepository
import com.example.weather_radar_app.internal.UnitSystem
import com.example.weather_radar_app.internal.lazyDeferred
import com.example.weather_radar_app.provider.UnitProvider

class HomeViewModel(private val repository: CurrentWeatherRepository, unitProvider: UnitProvider) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        repository.getCurrentWeather(unitSystem.designation)
    }
    val weatherLocation by lazyDeferred {
        repository.getWeatherLocation()
    }
}