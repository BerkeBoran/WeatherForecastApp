package com.example.weatherforecastapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    val currentWeather = MutableLiveData<WeatherInfo>()
    val hourlyForecast = MutableLiveData<List<HourlyWeather>>()
    val dailyForecast = MutableLiveData<List<DailyWeather>>()
    private val apiKey = "714fedf8dd76716b056677cd9ca8dbf8"

    fun fetchWeatherData(lat: Double,lon: Double){
    viewModelScope.launch {
        try {
            val weatherRes= RetrofitClient.apiService.getCurrentWeatherByCoord(lat,lon,apiKey)
            val forecastRes= RetrofitClient.apiService.getForecastByCoord(lat,lon,apiKey)

            weatherRes.body()?.let {
                data->
                currentWeather.postValue(
                    WeatherInfo(
                    cityName = data.name,
                     temp = data.main.temp,
                       tempMax = data.main.temp_max,
                        tempMin = data.main.temp_min,
                        description =data.weather[0].description,
                        icon = data.weather[0].icon,

                    )

                )
            }
            forecastRes.body()?.let {
                data->
                val hourlyList=data.list.take(8).map {
                    HourlyWeather(it.dt_txt.substring(11,16),it.main.temp,it.weather[0].icon)
                }
                hourlyForecast.postValue(hourlyList)
                val dailyMap = data.list.groupBy { it.dt_txt.substring(0, 10) }
                val dailyList = dailyMap.entries.take(5).map {
                    val temps = it.value.map { f -> f.main.temp }

                    DailyWeather(it.key, temps.maxOrNull() ?: 0.0, temps.minOrNull() ?: 0.0, it.value.first().weather[0].icon)
                }
                dailyForecast.postValue(dailyList)
            }


            }catch (e: Exception) {
            Log.e("WeatherViewModel", "Error: ${e.message}")
        }

        }
    }


    }

