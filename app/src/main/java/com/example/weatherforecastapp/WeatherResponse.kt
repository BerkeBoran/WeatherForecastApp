package com.example.weatherforecastapp

data class WeatherResponse(val name: String,
    val main: Main,
    val weather: List<Weather>
)

data class Main(val temp: Double, val temp_min: Double, val temp_max: Double)

data class Weather(val description: String, val icon: String)
