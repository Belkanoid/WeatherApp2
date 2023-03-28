package com.belkanoid.weatherapp2.data.remote

import com.belkanoid.weatherapp2.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherFactory {

    val service: WeatherApi by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(WeatherInterceptor())
            .build()

        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(WeatherApi::class.java)
    }
}