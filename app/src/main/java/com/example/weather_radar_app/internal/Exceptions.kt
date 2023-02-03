package com.example.weather_radar_app.internal

import java.io.IOException


class NoConnectivityException: IOException()
class LocationPermissionNotGrantedException: Exception()
class DateNotFoundException: Exception()