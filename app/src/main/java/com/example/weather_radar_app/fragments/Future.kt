package com.example.weather_radar_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_radar_app.R
import com.example.weather_radar_app.base.ScopedFragment
import com.example.weather_radar_app.data.db.entity.FutureWeatherEntry
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_future.*
import kotlinx.android.synthetic.main.fragment_home.groupLoading
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class Future : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: FutureViewModelFactory by instance()
    private lateinit var viewModel: FutureViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_future, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FutureViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = launch {
        val futureWeatherEntries = viewModel.weatherEntries.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(viewLifecycleOwner, Observer { location ->
            if (location == null) {
                return@Observer
            }
            updateLocation(location.timezoneId)
        })
        futureWeatherEntries.observe(viewLifecycleOwner, Observer { weatherEntries ->
            if (weatherEntries == null) {
                return@Observer
            }
            groupLoading.visibility = View.GONE
            updateDateToNextWeek()
            initRecyclerView(weatherEntries.toFutureWeatherItems())
        })
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }
    private fun updateDateToNextWeek() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Next Week"
    }
    private fun List<FutureWeatherEntry>.toFutureWeatherItems(): List<FutureWeatherItem> {
        return this.map { FutureWeatherItem(it) }
    }

    private fun initRecyclerView(items: List<FutureWeatherItem>) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@Future.context)
            adapter = groupAdapter
        }
        groupAdapter.setOnItemClickListener { item, view ->
            (item as? FutureWeatherItem)?.let {  showWeatherDetail(it.weatherEntry.date, view) }
        }
    }

    private fun showWeatherDetail(date: Long, view: View) {
        val actionDetail = FutureDirections.actionDetail(date)
        Navigation.findNavController(view).navigate(actionDetail)
    }
}