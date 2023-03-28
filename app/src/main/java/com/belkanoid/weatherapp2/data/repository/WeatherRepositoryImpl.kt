package com.belkanoid.weatherapp2.data.repository

import com.belkanoid.weatherapp2.data.mapper.toWeatherInfo
import com.belkanoid.weatherapp2.data.remote.WeatherApi
import com.belkanoid.weatherapp2.domain.repository.WeatherRepository
import com.belkanoid.weatherapp2.domain.util.Result
import com.belkanoid.weatherapp2.domain.util.rethrowCancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class WeatherRepositoryImpl @Inject constructor(
    private val service: WeatherApi
) : WeatherRepository {

    private var code = 200
    override suspend fun fetchWeatherInfo(city: String): Result = withContext(Dispatchers.IO) {
        try {
            Result.Success(
                data = service.getWeatherInfo(city)
                    .also { code = it.code() }
                    .body()!!.toWeatherInfo()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            e.rethrowCancellationException()
            Result.Error(
                message = when (code) {
                    404 -> "City not found"
                    else -> e.message ?: "Unknown error"
                }
            )
        }
    }
}