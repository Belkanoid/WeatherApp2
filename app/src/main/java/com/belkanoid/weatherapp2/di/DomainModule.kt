package com.belkanoid.weatherapp2.di

import com.belkanoid.weatherapp2.domain.util.ConnectivityObserver
import com.belkanoid.weatherapp2.presentation.NetworkConnectivityObserver
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {


    @Binds
    fun bindNetworkConnectivityObserver(networkConnectivityObserver: NetworkConnectivityObserver): ConnectivityObserver
}