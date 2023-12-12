package com.belkanoid.weatherapp2.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belkanoid.weatherapp2.domain.repository.WeatherRepository
import com.belkanoid.weatherapp2.domain.util.ConnectivityObserver
import com.belkanoid.weatherapp2.domain.util.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val connectivityObserver: ConnectivityObserver,
) : ViewModel() {

    private val networkStatus = MutableStateFlow(ConnectivityObserver.Status.Unavailable)
    private val loadingFlow = MutableSharedFlow<State>()
    private var searchJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val state = repository.weatherInfoFlow
        .mapLatest {
            when (it) {
                is Result.Success -> State.Success(it.data)
                is Result.Error -> State.Error(it.message)
            }
        }
        .mergeWith(loadingFlow)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = State.Idle
        )

    init {
        connectivityObserver.observe()
            .onEach { networkStatus.value = it }
            .launchIn(viewModelScope)
    }

    private fun isInternetAvailable() = networkStatus.value == ConnectivityObserver.Status.Available

    fun getWeatherInfo(city: CharSequence) {
        searchJob?.cancel(message = "Started new search")
        searchJob = viewModelScope.launch {
            delay(1000)
            if (!isInternetAvailable()) {
                loadingFlow.emit(State.Error(message = "Связь не доступна"))
                return@launch
            }
            if (city.isEmpty()) {
                loadingFlow.emit(State.Idle)
                return@launch
            }
            loadingFlow.emit(State.Loading)
            repository.getWeatherInfo(city.toString().trim())
        }
    }
}