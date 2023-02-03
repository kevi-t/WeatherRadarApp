package com.example.weather_radar_app.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather_radar_app.data.repository.CurrentWeatherRepository
import com.example.weather_radar_app.provider.UnitProvider

class HomeViewModelFactory(private val currentWeatherRepository: CurrentWeatherRepository, private val unitProvider: UnitProvider) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(currentWeatherRepository, unitProvider) as T
    }
}