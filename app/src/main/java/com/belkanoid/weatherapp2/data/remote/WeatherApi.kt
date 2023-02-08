package com.belkanoid.weatherapp2.data.remote

import com.belkanoid.weatherapp2.data.remote.dto.WeatherInfoDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getWeatherInfo(@Query("q") city: String): WeatherInfoDto
}