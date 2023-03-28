package com.belkanoid.weatherapp2.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belkanoid.weatherapp2.domain.repository.WeatherRepository
import com.belkanoid.weatherapp2.domain.state.Action
import com.belkanoid.weatherapp2.domain.state.State
import com.belkanoid.weatherapp2.domain.state.StateMachine
import com.belkanoid.weatherapp2.domain.util.ConnectivityObserver
import com.belkanoid.weatherapp2.domain.util.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val connectivityObserver: ConnectivityObserver,
) : ViewModel(), StateMachine.StateMachineListener {

    private val stateMachine = StateMachine(this)
    private val _networkStatus = MutableStateFlow(ConnectivityObserver.Status.Unknown)
    private val _state = MutableStateFlow<State>(State.Idle)
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                _networkStatus.value = status
            }
        }
    }

    override suspend fun fetchData(city: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (!isInternetAvailable()) return@launch

            delay(400L)
            fetchWeatherInfo(city)
        }
    }

    override fun dispatchState(newState: State) {
        viewModelScope.launch(Dispatchers.IO) { _state.emit(newState) }
    }

    private suspend fun fetchWeatherInfo(city: String) {
        when (val result = repository.fetchWeatherInfo(city)) {
            is Result.Success -> {
                stateMachine.dispatch(Action.LoadSuccess(data = result.data))
            }
            is Result.Error -> {
                stateMachine.dispatch(Action.LoadFailure(message = result.message))
            }
        }
    }

    private suspend fun isInternetAvailable(): Boolean {
        if (_networkStatus.value == ConnectivityObserver.Status.Available) {
            return true
        }
        stateMachine.dispatch(Action.LoadFailure(message = "Please check your Internet connection"))
        return false
    }

    fun getWeatherInfo(city: String) {
        viewModelScope.launch {
            if (city.isBlank()){
                searchJob?.cancel()
                stateMachine.dispatch(Action.BackToIdle)
                return@launch
            }
            stateMachine.dispatch(Action.LoadWeatherInfo(city = city))
        }
    }
}