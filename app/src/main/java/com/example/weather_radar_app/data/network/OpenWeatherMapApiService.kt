package com.example.weather_radar_app.data.network

import com.example.weather_radar_app.data.network.response.FutureWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val OPEN_WEATHER_MAP_APP_ID = "6fae27a4fc53a2dc7a698eee0d068605"
const val EXCLUDE_QUERY = "current,minutely,hourly"
//https://api.openweathermap.org/data/2.5/onecall?lat=0.5635&lon=34.5606&exclude=current,minutely,hourly&appid=***

interface OpenWeatherMapApiService {

    @GET("onecall")
    fun getFutureWeather( @Query("lat") lat: Double,@Query("lon") lon: Double,@Query("units") units: String): Deferred<FutureWeatherResponse>
    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): OpenWeatherMapApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("exclude",EXCLUDE_QUERY)
                    .addQueryParameter("appid",OPEN_WEATHER_MAP_APP_ID)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherMapApiService::class.java)
        }
    }
}