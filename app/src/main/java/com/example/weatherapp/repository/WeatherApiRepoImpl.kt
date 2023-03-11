package com.example.weatherapp.repository

class WeatherApiRepoImpl(private val api: WeatherApi) : RetrofitSafeCall() {

    suspend fun getWeatherForecast(
        latitude: Double,
        longitude: Double,
        apiKey: String
    ) = safeApiCall {
        api.getWeatherForecast(latitude, longitude, apiKey)
    }
}