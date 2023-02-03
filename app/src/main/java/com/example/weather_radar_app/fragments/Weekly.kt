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
import com.example.weather_radar_app.data.db.entity.FutureWeatherEntry
import kotlinx.android.synthetic.main.fragment_weekly.*
import com.example.weather_radar_app.internal.DateNotFoundException
import com.example.weather_radar_app.internal.glide.GlideApp
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class Weekly : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactoryInstanceFactory: ((Long) -> WeeklyViewModelFactory) by factory()
    private lateinit var viewModel: WeeklyViewModel

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weekly, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       val safeArgs = arguments?.let {  WeeklyArgs.fromBundle(it) }
       val date = safeArgs?.dateLong ?: throw DateNotFoundException()
       viewModel = ViewModelProvider(this,viewModelFactoryInstanceFactory(date)).get(WeeklyViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = launch {
        val futureWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(viewLifecycleOwner, Observer { location ->
            if (location == null) {
                return@Observer
            }
            updateLocation(location.timezoneId)
        })
        futureWeather.observe(viewLifecycleOwner, Observer { weatherEntry ->
            if (weatherEntry == null) {
                return@Observer
            }
            updateDate(weatherEntry)
            updateTemperatures( weatherEntry.temp.day,weatherEntry.temp.min, weatherEntry.temp.max)
            updateCondition("Condition")
            updateHumidity(weatherEntry.humidity)
            updateDewPoint(weatherEntry.dewPoint)
            updateWindSpeed(weatherEntry.windSpeed)
            updateUv(weatherEntry.uvi)
            GlideApp.with(this@Weekly).load("http:" + weatherEntry.conditionIconUrl).into(imageViewConditionIcon)
        })
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetric) metric else imperial
    }
    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }
    private fun updateDate(weatherEntry: FutureWeatherEntry) {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =  weatherEntry.getLocalDate("Africa/Nairobi").format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    }
    private fun updateTemperatures(temperature: Double, min: Double, max: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        textViewTemperature.text = "$temperature$unitAbbreviation"
        textViewMinMaxTemperature.text = "Min: $min$unitAbbreviation, Max: $max$unitAbbreviation"
    }
    private fun updateCondition(condition: String) {
        textViewCondition.text = condition
    }
    private fun updateHumidity(humid: Double){
        textViewPrecipitation.text = "Humidity: $humid"
    }
    private fun updateDewPoint(dewPoint: Double) {
        textViewVisibility.text ="Dew: $dewPoint"
    }

    private fun updateWindSpeed(windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        textViewWind.text = "Wind speed: $windSpeed $unitAbbreviation"
    }
    private fun updateUv(uv: Double) {
        textViewUv.text = "UV: $uv"
    }
}