package com.example.weatherapp.model

data class WeatherForecast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<DataList>,
    val message: Int
)


