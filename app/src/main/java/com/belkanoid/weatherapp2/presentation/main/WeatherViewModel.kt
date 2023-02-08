package com.belkanoid.weatherapp2.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belkanoid.weatherapp2.domain.repository.WeatherRepository
import com.belkanoid.weatherapp2.domain.util.ConnectivityObserver
import com.belkanoid.weatherapp2.domain.util.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val connectivityObserver: ConnectivityObserver,
) : ViewModel() {

    private var _state = MutableStateFlow<WeatherState>(WeatherState.Empty)
    val state = _state.asStateFlow()
    private val _networkStatus = MutableStateFlow(ConnectivityObserver.Status.Available)

    init {
        viewModelScope.launch {
            connectivityObserver.observe().collect{status->
                Log.d("ABCv", status.toString())
                when(status) {
                    ConnectivityObserver.Status.Available -> {
                        _networkStatus.value = status
                    }
                    ConnectivityObserver.Status.Losing -> {
                        _networkStatus.value = status
                    }
                    ConnectivityObserver.Status.Lost -> {
                        _networkStatus.value = status
                    }
                    ConnectivityObserver.Status.Unavailable -> {
                        _networkStatus.value = status
                    }
                }
            }
        }
    }

    private var searchJob: Job? = null

    private fun getWeatherInfo(city: String) {
        _state.value = WeatherState.Loading


        if (_networkStatus.value != ConnectivityObserver.Status.Available) {
            _state.value = WeatherState.Error.NetworkError("Check Internet connection")
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400L)
            when (val result = repository.getWeatherInfo(city)) {
                is Result.Success -> {
                    _state.value = WeatherState.Success(result.data)
                }
                is Result.Error -> {
                    _state.value = WeatherState.Error.TypeError(result.message ?: "Unknown error, try again...")
                }
            }
        }
    }

    fun onEvent(event: MainEvent) {
        when(event) {
            is MainEvent.SearchByCityEvent -> {
                getWeatherInfo(event.query)
            }
        }
    }


}