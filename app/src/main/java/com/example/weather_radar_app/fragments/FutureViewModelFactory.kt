package com.example.weather_radar_app.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather_radar_app.data.repository.FutureWeatherRepository
import com.example.weather_radar_app.provider.UnitProvider

class FutureViewModelFactory(private val repository: FutureWeatherRepository, private val unitProvider: UnitProvider) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FutureViewModel(repository, unitProvider) as T
    }
}