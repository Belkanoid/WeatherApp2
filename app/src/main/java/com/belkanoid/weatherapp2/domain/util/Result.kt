package com.belkanoid.weatherapp2.domain.util

import com.belkanoid.weatherapp2.domain.model.WeatherInfo

sealed class Result {
    data class Success(val data: WeatherInfo): Result()
    data class Error(val message: String): Result()
}
