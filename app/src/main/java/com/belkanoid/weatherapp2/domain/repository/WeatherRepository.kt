package com.belkanoid.weatherapp2.domain.repository

import com.belkanoid.weatherapp2.domain.util.Result
import kotlinx.coroutines.flow.SharedFlow

interface WeatherRepository {

    val weatherInfoFlow: SharedFlow<Result>
    suspend fun getWeatherInfo(city: String)
}