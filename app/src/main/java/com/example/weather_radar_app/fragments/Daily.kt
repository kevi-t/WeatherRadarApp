package com.example.weather_radar_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weather_radar_app.databinding.FragmentDailyBinding
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class Daily : Fragment() {
    private lateinit var binding: FragmentDailyBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentDailyBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.button.setOnClickListener{
            findWeather()
        }
        return view
    }


    private fun findWeather() {
        val city: String = binding.searchBar.text.toString()
        val url ="http://api.openweathermap.org/data/2.5/weather?q=$city&appid=6fae27a4fc53a2dc7a698eee0d068605&units=metric"
        val stringRequest = StringRequest(Request.Method.GET, url,{ response ->
            try {
                //find temperature
                val jsonObject = JSONObject(response)
                val `object` = jsonObject.getJSONObject("main")
                val temp = `object`.getDouble("temp")
                binding.textView3.text = "Temperature\n$temp°C"

                //find country
                val object8 = jsonObject.getJSONObject("sys")
                val count = object8.getString("country")
                binding.country.text = "$count  :"

                //find city
                val city = jsonObject.getString("name")
                binding.cityName.text = city

                //find icon
                val jsonArray = jsonObject.getJSONArray("weather")
                val obj = jsonArray.getJSONObject(0)
                val icon = obj.getString("icon")
                Picasso.get().load("http://openweathermap.org/img/wn/$icon@2x.png").into(binding.imageView)

                //find date & time
                val calendar = Calendar.getInstance()
                val std = SimpleDateFormat("HH:mm a \nE, MMM dd yyyy")
                val date = std.format(calendar.time)
                binding.textView2.text = date

                //find latitude
                val object2 = jsonObject.getJSONObject("coord")
                val lat_find = object2.getDouble("lat")
                binding.latitude.text = "$lat_find°  N"

                //find longitude
                val object3 = jsonObject.getJSONObject("coord")
                val long_find = object3.getDouble("lon")
                binding.longitude.text = "$long_find°  E"

                //find humidity
                val object4 = jsonObject.getJSONObject("main")
                val humidity_find = object4.getInt("humidity")
                binding.humidity.text = "$humidity_find  %"

                //find sunrise
                val object5 = jsonObject.getJSONObject("sys")
                val sunrise_find = object5.getString("sunrise")
                binding.sunrise.text = "$sunrise_find  am"

                //find sunrise
                val object6 = jsonObject.getJSONObject("sys")
                val sunset_find = object6.getString("sunset")
                binding.sunset.text = "$sunset_find  pm"

                //find pressure
                val object7 = jsonObject.getJSONObject("main")
                val pressure_find = object7.getString("pressure")
                binding.pressure.text = "$pressure_find  hPa"

                //find wind speed
                val object9 = jsonObject.getJSONObject("wind")
                val wind_find = object9.getString("speed")
                binding.wind.text = "$wind_find  km/h"

                //find min temperature
                val object10 = jsonObject.getJSONObject("main")
                val mintemp = object10.getDouble("temp_min")
                binding.minTemp.text = "Min Temp\n$mintemp °C"

                //find max temperature
                val object12 = jsonObject.getJSONObject("main")
                val maxtemp = object12.getDouble("temp_max")
                binding.tempMax.text = "Max Temp\n$maxtemp °C"

                //find feels
                val object13 = jsonObject.getJSONObject("main")
                val feels_find = object13.getDouble("feels_like")
                binding.feels.text = "$feels_find °C"
            }
            catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { error ->  Toast.makeText(context, error.localizedMessage, Toast.LENGTH_SHORT).show() }
        val requestQueue = Volley.newRequestQueue(requireView().context)
        requestQueue.add(stringRequest)
    }


}