package com.belkanoid.weatherapp2.presentation.main

sealed class MainEvent {
    class SearchByCityEvent(val query: String): MainEvent()
}
