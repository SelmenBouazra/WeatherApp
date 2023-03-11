package com.example.weatherapp.repository

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Builder {

    companion object{
        private val oktHttpClient = OkHttpClient.Builder()
        private const val SERVER_URL = "https://api.openweathermap.org/"
        fun get(): Retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(oktHttpClient.build())
            .build()
    }
}