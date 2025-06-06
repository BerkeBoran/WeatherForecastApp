package com.example.weatherforecastapp
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherByCoord(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "tr"
    ): Response<WeatherResponse>

    @GET("data/2.5/forecast")
    suspend fun getForecastByCoord(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "tr"
    ): Response<ForecastResponse>
}