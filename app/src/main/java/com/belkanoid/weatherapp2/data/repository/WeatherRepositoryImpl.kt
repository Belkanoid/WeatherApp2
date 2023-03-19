package com.belkanoid.weatherapp2.data.repository

import com.belkanoid.weatherapp2.data.mapper.toWeatherInfo
import com.belkanoid.weatherapp2.data.remote.WeatherApi
import com.belkanoid.weatherapp2.domain.repository.WeatherRepository
import com.belkanoid.weatherapp2.domain.util.Result
import com.belkanoid.weatherapp2.domain.util.rethrowCancellationException
import javax.inject.Inject


class WeatherRepositoryImpl @Inject constructor(
    private val service: WeatherApi
) : WeatherRepository {

    override suspend fun fetchWeatherInfo(city: String): Result =
        try {
            Result.Success(
                data = service.getWeatherInfo(city).toWeatherInfo()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            e.rethrowCancellationException()
            Result.Error(
                message = e.message ?: "Unknown error"
            )
        }

}