package com.netchar.pixally.infrastructure

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


class InternetChecker(private val applicationContext: Context) : NetworkConnectivityChecker {

    override val isConnected: Boolean
        get() = hasInternetConnection(applicationContext)

    private fun hasInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isInternetConnected(connectivityManager)
        } else {
            isInternetConnectedLegacy(connectivityManager)
        }
    }

    private fun isInternetConnected(connectivityManager: ConnectivityManager): Boolean {
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return hasNetwork(networkCapabilities)
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun hasNetwork(networkCapabilities: NetworkCapabilities?): Boolean {
        if (networkCapabilities == null) {
            return false
        }
        val transports = listOf(NetworkCapabilities.TRANSPORT_WIFI, NetworkCapabilities.TRANSPORT_CELLULAR)
        return transports.any { networkCapabilities.hasTransport(it) }
    }

    private fun isInternetConnectedLegacy(connectivityManager: ConnectivityManager): Boolean {
        val nwInfo = connectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }
}

interface NetworkConnectivityChecker {
    val isConnected: Boolean
}