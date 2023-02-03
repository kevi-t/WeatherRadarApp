package com.example.weather_radar_app.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather_radar_app.data.repository.FutureWeatherRepository
import com.example.weather_radar_app.provider.UnitProvider

class WeeklyViewModelFactory(private val detailDate: Long, private val repository: FutureWeatherRepository, private val unitProvider: UnitProvider) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeeklyViewModel(detailDate, repository, unitProvider) as T
    }
}