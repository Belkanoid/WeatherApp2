package com.belkanoid.weatherapp2.data.remote

interface WeatherApi {

    suspend fun getWeatherInfo(city: String)
}