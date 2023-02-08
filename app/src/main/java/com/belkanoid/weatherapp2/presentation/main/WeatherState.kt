package com.belkanoid.weatherapp2.presentation.main

import com.belkanoid.weatherapp2.domain.model.WeatherInfo

sealed class WeatherState {
    class Success(val data: WeatherInfo? = null) : WeatherState()
    sealed class Error(val message: String) : WeatherState() {
        class NetworkError(message: String): Error(message)
        class TypeError(message: String): Error(message)
    }
    object Loading : WeatherState()
    object Empty : WeatherState()
}
