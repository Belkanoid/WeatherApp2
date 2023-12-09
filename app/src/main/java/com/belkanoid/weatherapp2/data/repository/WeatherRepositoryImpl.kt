package com.belkanoid.weatherapp2.data.repository

import com.belkanoid.weatherapp2.data.mapper.toWeatherInfo
import com.belkanoid.weatherapp2.data.remote.helper.WeatherApiHelper
import com.belkanoid.weatherapp2.domain.repository.WeatherRepository
import com.belkanoid.weatherapp2.domain.util.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class WeatherRepositoryImpl @Inject constructor(
    private val service: WeatherApiHelper
) : WeatherRepository {

    private val _weatherInfoFlow = MutableSharedFlow<Result>()
    override val weatherInfoFlow = _weatherInfoFlow.asSharedFlow()

    override suspend fun getWeatherInfo(city: String) {
        val result = service.getWeatherInfo(city)
            .map { Result.Success(data = it.toWeatherInfo()) as Result }
            .catch { emit(Result.Error(message = "Неизвестная ошибка")) }

        result.collect{
            _weatherInfoFlow.emit(it)
        }
    }
}