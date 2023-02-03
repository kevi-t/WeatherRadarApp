package com.example.weather_radar_app.fragments

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_future_weather.*
import com.example.weather_radar_app.R
import com.example.weather_radar_app.data.db.entity.FutureWeatherEntry
import com.example.weather_radar_app.internal.glide.GlideApp
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle


class FutureWeatherItem(val weatherEntry: FutureWeatherEntry) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textViewCondition.text = "condition"
            updateDate()
            updateTemperature()
            updateConditionImage()
        }
    }
    override fun getLayout() = R.layout.item_future_weather
    private fun ViewHolder.updateDate() {
        val dtFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        textViewDate.text = weatherEntry.getLocalDate("Africa/Nairobi").format(dtFormatter)
    }
    private fun ViewHolder.updateTemperature() {
        val unitAbbreviation = "°C"
        textViewTemperature.text = "${weatherEntry.temp.day.toInt()}$unitAbbreviation"
    }
    private fun ViewHolder.updateConditionImage() {
        GlideApp.with(this.containerView).load("http:" + weatherEntry.conditionIconUrl).into(imageViewConditionIcon)
    }
}