package com.example.weatherapp.repository

import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String = "No Internet Connection"
}