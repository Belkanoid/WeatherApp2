package com.belkanoid.weatherapp2.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belkanoid.weatherapp2.R
import com.belkanoid.weatherapp2.domain.repository.WeatherRepository
import com.belkanoid.weatherapp2.domain.state.Action
import com.belkanoid.weatherapp2.domain.state.State
import com.belkanoid.weatherapp2.domain.state.StateMachine
import com.belkanoid.weatherapp2.domain.util.ConnectivityObserver
import com.belkanoid.weatherapp2.domain.util.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val connectivityObserver: ConnectivityObserver,
) : ViewModel(), StateMachine.StateMachineListener {

    private val stateMachine = StateMachine(this)
    private val networkStatus = MutableStateFlow(ConnectivityObserver.Status.Available)
    private val _state = MutableStateFlow<State>(State.Idle)
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        connectivityObserver.observe()
            .onEach { status ->
                checkInternetState(status)
                networkStatus.value = status
            }
            .launchIn(viewModelScope)
    }

    private suspend fun checkInternetState(status: ConnectivityObserver.Status) {
        val availableStatus = ConnectivityObserver.Status.Available
        val isBackToOnline = networkStatus.value != availableStatus && status == availableStatus
        val isGoneToOffline = networkStatus.value == availableStatus && status != availableStatus
        if (isBackToOnline) {
            _state.emit(State.InternetStatus("Снова в сети", R.color.backToOnlineBackground))
        }
        if (isGoneToOffline) {
            _state.emit(State.InternetStatus("Связь пропала", R.color.errorBackground))
        }
    }

    private fun isInternetAvailable() = networkStatus.value == ConnectivityObserver.Status.Available

    override fun fetchData(city: String) {
        searchJob?.cancel()
        if (!isInternetAvailable()) {
            stateMachine.dispatch(Action.LoadFailure("Please check your Internet connection"))
            return
        }
        searchJob = viewModelScope.launch {
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

    fun getWeatherInfo(city: String) {
        city.takeIf { it.isBlank() }
            ?.let {
                searchJob?.cancel()
                stateMachine.dispatch(Action.BackToIdle)
                return
            }

        viewModelScope.launch {
            stateMachine.dispatch(Action.LoadWeatherInfo(city = city))
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}