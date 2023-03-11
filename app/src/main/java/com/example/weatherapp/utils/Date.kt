package com.example.weatherapp.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toDate(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val localDateTime = LocalDateTime.parse(this, formatter)
    return localDateTime.dayOfMonth.toString() + "  " + localDateTime.month.toString().lowercase() + "  " + localDateTime.dayOfWeek.toString().lowercase()
}

fun String.toTime(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val localDateTime = LocalDateTime.parse(this, formatter)
    return localDateTime.toLocalTime().toString()
}

fun String.toDay(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val localDateTime = LocalDateTime.parse(this, formatter)
    return localDateTime.dayOfWeek.toString().lowercase()
}

fun String.toCompleteDate(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val localDateTime = LocalDateTime.parse(this, formatter)
    return localDateTime.toLocalDate().toString()
}


