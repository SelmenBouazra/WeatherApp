package com.example.weatherapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.viewbinding.BuildConfig
import com.example.weatherapp.model.DataList
import com.example.weatherapp.model.WeatherForecast
import com.example.weatherapp.model.WeatherInfo
import com.example.weatherapp.repository.Builder
import com.example.weatherapp.repository.Resource
import com.example.weatherapp.repository.WeatherApi
import com.example.weatherapp.repository.WeatherApiRepoImpl
import com.example.weatherapp.utils.toCelsius
import com.example.weatherapp.utils.toDay
import kotlinx.coroutines.flow.MutableSharedFlow

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _weatherForecast = MutableSharedFlow<Resource<WeatherForecast>>()
    val weatherForecast get() = _weatherForecast

    private val _selectedDay = MutableSharedFlow<String>()
    val selectedDay get() = _selectedDay

    private var weatherApi = WeatherApiRepoImpl(
        Builder.get().create(
            WeatherApi::class.java
        )
    )

    suspend fun getWeatherForecast(
        latitude: Double,
        longitude: Double,
    ){
        val apiKey = "b7ac52663423e73f6c5624b6161035c5"
        _weatherForecast.emit(Resource.Loading())
        _weatherForecast.emit(weatherApi.getWeatherForecast(latitude, longitude, apiKey))
    }

    fun getDaysWeatherInfo(dataList: List<DataList>): List<WeatherInfo> {
        val groupedData = dataList.groupBy { it.dt_txt.substringBefore(' ') }
        return groupedData.map { (_, data) ->
            val description = data.first().weather.first().description
            val minTemp = data.minOfOrNull { it.main.temp_min } ?: 0.0
            val maxTemp = data.maxOfOrNull { it.main.temp_max } ?: 0.0
            val temperature = "${minTemp.toCelsius()} / ${maxTemp.toCelsius()} °C"
            val icon = data.first().weather.first().id
            val day = data.first().dt_txt
            WeatherInfo(day, description, icon, temperature)
        }
    }
}