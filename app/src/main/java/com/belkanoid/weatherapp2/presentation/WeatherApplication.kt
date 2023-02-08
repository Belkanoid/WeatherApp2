package com.belkanoid.weatherapp2.presentation

import android.app.Application
import com.belkanoid.weatherapp2.di.DaggerWeatherComponent
import com.belkanoid.weatherapp2.di.WeatherComponent

class WeatherApplication: Application() {


    val component: WeatherComponent by lazy {
        DaggerWeatherComponent.factory().create(applicationContext)
    }
}