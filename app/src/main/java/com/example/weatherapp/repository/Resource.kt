package com.example.weatherapp.repository

import okhttp3.ResponseBody

sealed class Resource<out T> {

    data class Success<out T>(val value: T): Resource<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?,
        val errorCast: String?,
    ): Resource<Nothing>()
    data class Loading(val isLoading: Boolean = true): Resource<Nothing>()

}