package com.example.weatherapp.ui

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.model.DataList
import com.example.weatherapp.model.WeatherForecast
import com.example.weatherapp.repository.Resource
import com.example.weatherapp.ui.adapter.DaysAdapter
import com.example.weatherapp.ui.adapter.WeatherStateAdapter
import com.example.weatherapp.utils.checkForPermission
import com.example.weatherapp.utils.getUserLocation
import com.example.weatherapp.utils.showPopupRequirePermission
import com.example.weatherapp.utils.toCelsius
import com.example.weatherapp.utils.toCompleteDate
import com.example.weatherapp.utils.toDate
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val weatherStateAdapter = WeatherStateAdapter()
    private val daysAdapter = DaysAdapter()
    private var dataList : List<DataList>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapters()

        lifecycleScope.launch {
            viewModel.weatherForecast.collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.shimmerViewContainer.visibility = View.VISIBLE
                        binding.container.visibility = View.GONE
                    }

                    is Resource.Success -> {
                        binding.shimmerViewContainer.visibility = View.GONE
                        binding.container.visibility = View.VISIBLE
                        initView(result.value)
                        dataList  = result.value.list
                        weatherStateAdapter.setList(result.value.list.filter {
                            it.dt_txt.toCompleteDate() == result.value.list.first().dt_txt.toCompleteDate()
                        })
                        daysAdapter.setList(viewModel.getDaysWeatherInfo(result.value.list))
                    }

                    is Resource.Failure -> {
                        binding.shimmerViewContainer.visibility = View.GONE
                        binding.container.visibility = View.VISIBLE

                        val errorMessage = when {
                            result.isNetworkError -> {
                                "Please check your connection !"
                            }
                            result.errorBody != null -> {
                                Log.i("MainActivity", result.errorBody.toString())
                                result.errorBody.toString()

                            }
                            else -> {
                                Log.i("MainActivity", result.errorCast.toString())
                                "Please try again"

                            }
                        }
                        Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.selectedDay.collect{day ->
                dataList?.filter {dataList ->
                    dataList.dt_txt.toCompleteDate() == day.toCompleteDate()
                }?.let { it1 -> weatherStateAdapter.setList(it1) }
            }
        }


        checkForPermission(this, Manifest.permission.ACCESS_FINE_LOCATION,{
            requestMultiplePermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }) {
            getUserLocation(this, Context.LOCATION_SERVICE) { latitude, longitude ->
                lifecycleScope.launch {
                    viewModel.getWeatherForecast(latitude, longitude)
                }
            }
        }

    }

    private fun initAdapters() {
        binding.temperatureRv.adapter = weatherStateAdapter
        binding.daysRv.adapter = daysAdapter
        daysAdapter.onItemClick = {day ->
            lifecycleScope.launch{
                viewModel.selectedDay.emit(day)
            }
        }
    }

    private fun initView(data: WeatherForecast) {
        val firstDateList = data.list.first()
        binding.temperature.text = firstDateList.main.temp.toCelsius()
        binding.weatherDescription.text = firstDateList.weather.first().description

        (firstDateList.dt_txt.toDate() + "   " + data.city.name + ", " + data.city.country).also {
            binding.date.text = it
        }
    }


    private val requestMultiplePermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            when {
                result[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                    getUserLocation(this, Context.LOCATION_SERVICE) { latitude, longitude ->
                        lifecycleScope.launch {
                            viewModel.getWeatherForecast(latitude, longitude)
                        }
                    }
                }
                else -> {
                    showPopupRequirePermission(this)
                }
            }
        }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}




