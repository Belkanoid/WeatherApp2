package com.belkanoid.weatherapp2.data.remote.helper

import com.belkanoid.weatherapp2.data.remote.WeatherApi
import com.belkanoid.weatherapp2.data.remote.dto.WeatherInfoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherApiHelperImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherApiHelper{
    override fun getWeatherInfo(city: String): Flow<WeatherInfoDto> = flow {
        emit(weatherApi.getWeatherInfo(city))
    }
}