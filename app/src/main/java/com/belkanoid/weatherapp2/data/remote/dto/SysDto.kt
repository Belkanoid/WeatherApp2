package com.belkanoid.weatherapp2.data.remote.dto

data class SysDto(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)