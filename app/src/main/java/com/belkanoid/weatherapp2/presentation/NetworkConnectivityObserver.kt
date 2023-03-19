package com.belkanoid.weatherapp2.presentation

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.belkanoid.weatherapp2.domain.util.ConnectivityObserver
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class NetworkConnectivityObserver @Inject constructor(
    private val context: Context
) : ConnectivityObserver {

    private val networkJob: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val connectivityManager =
        context.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<ConnectivityObserver.Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    networkJob.launch { send(ConnectivityObserver.Status.Available) }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    networkJob.launch { send(ConnectivityObserver.Status.Losing) }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    networkJob.launch { send(ConnectivityObserver.Status.Lost) }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    networkJob.launch { send(ConnectivityObserver.Status.Unavailable) }
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
                networkJob.cancel()
            }
        }.distinctUntilChanged()
    }
}