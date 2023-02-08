package com.belkanoid.weatherapp2.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherFactory {

    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    val service: WeatherApi by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(WeatherInterceptor())
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(WeatherApi::class.java)
    }
}