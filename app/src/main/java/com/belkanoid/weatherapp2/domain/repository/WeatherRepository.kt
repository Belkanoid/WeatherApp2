package com.belkanoid.weatherapp2.domain.repository

import com.belkanoid.weatherapp2.domain.util.Result

interface WeatherRepository {
    suspend fun fetchWeatherInfo(city: String): Result
}