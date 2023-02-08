package com.belkanoid.weatherapp2.domain.repository

import com.belkanoid.weatherapp2.domain.model.WeatherInfo
import com.belkanoid.weatherapp2.domain.util.Result

interface WeatherRepository {
    suspend fun getWeatherInfo(city: String): Result<WeatherInfo>
}