package com.belkanoid.weatherapp2.domain.util

import android.net.ConnectivityManager
import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<Status>

    enum class Status {
        Unknown, Available, Unavailable, Losing, Lost
    }

}

