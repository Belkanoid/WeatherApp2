package com.belkanoid.weatherapp2.data.repository

import com.belkanoid.weatherapp2.data.remote.helper.WeatherApiHelper
import com.belkanoid.weatherapp2.domain.model.WeatherInfo
import com.belkanoid.weatherapp2.domain.repository.WeatherRepository
import com.belkanoid.weatherapp2.domain.util.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject


class WeatherRepositoryImpl @Inject constructor(
    private val service: WeatherApiHelper
) : WeatherRepository {

    private val _weatherInfoFlow = MutableSharedFlow<Result>()
    override val weatherInfoFlow = _weatherInfoFlow.asSharedFlow()

    override suspend fun getWeatherInfo(city: String) {
        service.getWeatherInfo(city)
            .collect {
                _weatherInfoFlow.emit(it)
            }
    }
}