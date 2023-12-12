package com.belkanoid.weatherapp2.data.remote.helper

import com.belkanoid.weatherapp2.data.mapper.toWeatherInfo
import com.belkanoid.weatherapp2.data.remote.WeatherApi
import com.belkanoid.weatherapp2.domain.model.WeatherInfo
import com.belkanoid.weatherapp2.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherApiHelperImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherApiHelper{
    override fun getWeatherInfo(city: String): Flow<Result> = flow {
        try {
            val result = weatherApi.getWeatherInfo(city)
            Result.Success(data = result.toWeatherInfo())
        }catch (e: Exception) {
            Result.Error(message = "Неизвестная ошибка")
        }

    }
}