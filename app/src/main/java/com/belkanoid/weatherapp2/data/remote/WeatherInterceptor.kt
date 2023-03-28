package com.belkanoid.weatherapp2.data.remote

import com.belkanoid.weatherapp2.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class WeatherInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newUrl = request.url().newBuilder()
            .addQueryParameter("appid", BuildConfig.API_KEY)
            .addQueryParameter("lang", RU)
            .addQueryParameter("units", UNITS)
            .build()

        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)

    }

    companion object {
        const val RU = "ru"
        const val EN = "en"
        const val UNITS = "metric"
    }
}