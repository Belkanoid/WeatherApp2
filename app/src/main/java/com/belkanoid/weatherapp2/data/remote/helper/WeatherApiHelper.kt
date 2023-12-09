package com.belkanoid.weatherapp2.data.remote.helper

import com.belkanoid.weatherapp2.data.remote.dto.WeatherInfoDto
import kotlinx.coroutines.flow.Flow

interface WeatherApiHelper {
    fun getWeatherInfo(city: String): Flow<WeatherInfoDto>
}