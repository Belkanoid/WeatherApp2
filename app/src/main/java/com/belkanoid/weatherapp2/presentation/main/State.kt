package com.belkanoid.weatherapp2.presentation.main

import com.belkanoid.weatherapp2.domain.model.WeatherInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge

sealed class State {
    object Idle: State()
    object Loading: State()
    data class Success(val data: WeatherInfo): State()
    data class Error(val message: String): State()
}

fun <T> Flow<T>.mergeWith(another: Flow<T>): Flow<T> {
    return merge(this, another)
}