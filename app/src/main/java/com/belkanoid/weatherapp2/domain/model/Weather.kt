package com.belkanoid.weatherapp2.domain.model

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)