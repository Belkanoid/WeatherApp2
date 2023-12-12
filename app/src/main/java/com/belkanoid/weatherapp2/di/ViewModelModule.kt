package com.belkanoid.weatherapp2.di

import androidx.lifecycle.ViewModel
import com.belkanoid.weatherapp2.presentation.main.WeatherViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@MapKey
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
interface ViewModelModule {

    @ViewModelKey(WeatherViewModel::class)
    @Binds
    @IntoMap
    fun bindWeatherViewModel(weatherViewModel: WeatherViewModel): ViewModel
}