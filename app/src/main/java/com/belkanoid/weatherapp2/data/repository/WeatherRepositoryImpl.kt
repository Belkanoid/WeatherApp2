package com.belkanoid.weatherapp2.data.repository

import android.util.Log
import com.belkanoid.weatherapp2.data.mapper.toWeatherInfo
import com.belkanoid.weatherapp2.data.remote.WeatherApi
import com.belkanoid.weatherapp2.domain.model.WeatherInfo
import com.belkanoid.weatherapp2.domain.repository.WeatherRepository
import com.belkanoid.weatherapp2.domain.util.Result
import javax.inject.Inject
import javax.inject.Singleton


class WeatherRepositoryImpl @Inject constructor(
    private val service: WeatherApi
) : WeatherRepository {

    override suspend fun getWeatherInfo(city: String): Result<WeatherInfo> =
        try {
            Result.Success(
                data = service.getWeatherInfo(city).toWeatherInfo()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(
                message = e.message ?: "Unknown error. Please check Internet is available"
            )
        }
}