package com.example.weather_radar_app

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import androidx.room.Room
import com.example.weather_radar_app.data.db.ForecastDatabase
import com.example.weather_radar_app.data.network.ConnectivityInterceptor
import com.example.weather_radar_app.data.network.ConnectivityInterceptorImpl
import com.example.weather_radar_app.data.network.OpenWeatherMapApiService
import com.example.weather_radar_app.data.network.WeatherStackApiService
import com.example.weather_radar_app.data.network.datasource.current.CurrentWeatherNetworkDataSource
import com.example.weather_radar_app.data.network.datasource.current.CurrentWeatherNetworkDataSourceImpl
import com.example.weather_radar_app.data.network.datasource.future.FutureWeatherNetworkDataSource
import com.example.weather_radar_app.data.network.datasource.future.FutureWeatherNetworkDataSourceImpl
import com.example.weather_radar_app.data.repository.CurrentWeatherRepository
import com.example.weather_radar_app.data.repository.CurrentWeatherRepositoryImpl
import com.example.weather_radar_app.data.repository.FutureWeatherRepository
import com.example.weather_radar_app.data.repository.FutureWeatherRepositoryImpl
import com.example.weather_radar_app.fragments.FutureViewModelFactory
import com.example.weather_radar_app.fragments.HomeViewModelFactory
import com.example.weather_radar_app.fragments.WeeklyViewModelFactory
import com.example.weather_radar_app.provider.LocationProvider
import com.example.weather_radar_app.provider.LocationProviderImpl
import com.example.weather_radar_app.provider.UnitProvider
import com.example.weather_radar_app.provider.UnitProviderImpl
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*


class ForecastApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))

        bind<ForecastDatabase>() with singleton { Room.databaseBuilder(this@ForecastApplication,ForecastDatabase::class.java,"forecast.db").build()}
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().futureWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().futureWeatherLocationDao() }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherLocationDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { WeatherStackApiService(instance()) }
        bind() from singleton { OpenWeatherMapApiService(instance()) }
        bind<CurrentWeatherNetworkDataSource>() with singleton { CurrentWeatherNetworkDataSourceImpl(weatherStackApiService = instance()) }
        bind<FutureWeatherNetworkDataSource>() with singleton { FutureWeatherNetworkDataSourceImpl(openWeatherMapApiService = instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<CurrentWeatherRepository>() with singleton { CurrentWeatherRepositoryImpl(currentWeatherDao = instance(),currentWeatherLocationDao = instance(),dataSource = instance(),locationProvider = instance())}
        bind<FutureWeatherRepository>() with singleton { FutureWeatherRepositoryImpl(futureWeatherDao = instance(),futureWeatherLocationDao = instance(),dataSource = instance(),locationProvider = instance()) }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from provider { HomeViewModelFactory(instance(), instance()) }
        bind() from provider { FutureViewModelFactory(instance(), instance()) }
        bind() from factory { detailDate: Long -> WeeklyViewModelFactory(detailDate, instance(), instance()) }

    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}