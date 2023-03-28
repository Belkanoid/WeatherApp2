package com.belkanoid.weatherapp2.domain.state

import com.belkanoid.weatherapp2.domain.model.WeatherInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


sealed class State {
    object Idle: State()
    object Loading: State()
    data class Success(val data: WeatherInfo): State()
    data class Error(val message: String): State()
}

sealed class Action {
    object BackToIdle: Action()
    data class LoadWeatherInfo(val city: String): Action()
    data class LoadSuccess(val data: WeatherInfo): Action()
    data class LoadFailure(val message: String): Action()
}

class StateMachine(
    private val stateMachineListener: StateMachineListener
) {

    suspend fun dispatch(action: Action) = withContext(Dispatchers.IO) {
        when(action) {
            is Action.LoadWeatherInfo -> {
                stateMachineListener.fetchData(action.city)
                stateMachineListener.dispatchState(State.Loading)
            }
            is Action.LoadSuccess -> {
                stateMachineListener.dispatchState(State.Success(action.data))
            }
            is Action.LoadFailure -> {
                stateMachineListener.dispatchState(State.Error(action.message))
            }
            is Action.BackToIdle -> {
                stateMachineListener.dispatchState(State.Idle)
            }
        }
    }

    interface StateMachineListener{
        suspend fun fetchData(city: String)
        fun dispatchState(newState: State)
    }
}


