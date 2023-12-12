package com.belkanoid.weatherapp2.data.remote.helper

import com.belkanoid.weatherapp2.domain.model.WeatherInfo
import com.belkanoid.weatherapp2.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface WeatherApiHelper {
    fun getWeatherInfo(city: String): Flow<Result>
}