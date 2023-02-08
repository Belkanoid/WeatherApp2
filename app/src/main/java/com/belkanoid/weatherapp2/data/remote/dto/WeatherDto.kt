package com.belkanoid.weatherapp2.data.remote.dto

data class WeatherDto(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)