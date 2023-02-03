package com.example.weather_radar_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather_radar_app.R
import com.example.weather_radar_app.base.ScopedFragment
import kotlinx.android.synthetic.main.fragment_home.*
import com.example.weather_radar_app.internal.glide.GlideApp
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class Home : ScopedFragment(),KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: HomeViewModelFactory by instance()
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(viewLifecycleOwner, Observer { location ->
                if (location != null) {
                    return@Observer updateLocation(location.name)
                }
        })

        currentWeather.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                return@Observer
            }
            groupLoading.visibility = View.GONE
            updateDateToToday()
            updateTemperature(it.temperature, it.feelslike)
            updateCondition(if (it.condition.isNotEmpty()) it.condition[0] else "")
            updatePrecipitation(it.precip)
            updateWind(it.windDir, it.windSpeed)
            updateVisibility(it.visibility)
            GlideApp.with(this@Home).load(if (it.icon.isNotEmpty()) it.icon[0] else "").into(imageViewConditionIcon)
        })
    }
    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetric) metric else imperial
    }
    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }
    private fun updateDateToToday() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }
    private fun updateTemperature(temperature: Double, feelsLike: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        textViewTemperature.text = "$temperature$unitAbbreviation"
        textViewFeelsLikeTemperature.text = "Feels like $feelsLike$unitAbbreviation"
    }
    private fun updateCondition(condition: String) {
        textViewCondition.text = condition
    }
    private fun updatePrecipitation(precipitationVolume: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        textViewPrecipitation.text = "Precipitation: $precipitationVolume $unitAbbreviation"
    }
    private fun updateWind(windDirection: String, windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        textViewWind.text = "Wind: $windDirection, $windSpeed $unitAbbreviation"
    }
    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km", "mi.")
        textViewVisibility.text = "Visibility: $visibilityDistance $unitAbbreviation"
    }

}