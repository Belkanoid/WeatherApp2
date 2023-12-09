package com.belkanoid.weatherapp2.di

import com.belkanoid.weatherapp2.data.remote.WeatherApi
import com.belkanoid.weatherapp2.data.remote.WeatherFactory
import com.belkanoid.weatherapp2.data.remote.helper.WeatherApiHelper
import com.belkanoid.weatherapp2.data.remote.helper.WeatherApiHelperImpl
import com.belkanoid.weatherapp2.data.repository.WeatherRepositoryImpl
import com.belkanoid.weatherapp2.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface DataModule {

    @Singleton
    @Binds
    fun bindWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    fun bindWeatherApiHelper(weatherApiHelperImpl: WeatherApiHelperImpl): WeatherApiHelper

    companion object {
        @Provides
        fun provideWeatherApi(): WeatherApi = WeatherFactory.service
    }
}