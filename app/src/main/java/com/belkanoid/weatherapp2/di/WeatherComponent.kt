package com.belkanoid.weatherapp2.di

import android.app.Application
import android.content.Context
import com.belkanoid.weatherapp2.presentation.main.MainActivity
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [DataModule::class, DomainModule::class, ViewModelModule::class])
interface WeatherComponent {

    fun inject(mainActivity: MainActivity)


    @Component.Factory
    interface Factory{
        fun create(@BindsInstance applicationContext: Context): WeatherComponent
    }

}