package com.netchar.pixally.infrastructure

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


class InternetChecker(private val applicationContext: Context) : NetworkConnectivityChecker {

    override val isConnectionOn: Boolean
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

    override val isInternetAvailable: Boolean
        get() = isNetworkReachable()

    private fun isNetworkReachable(): Boolean {
        return try {
            val timeoutMs = 1500
            val socket = Socket()

            socket.use {
                it.connect(InetSocketAddress("8.8.8.8", 53), timeoutMs)
            }
            true
        } catch (e: IOException) {
            false
        }
    }
}

interface NetworkConnectivityChecker {
    val isConnectionOn: Boolean
    val isInternetAvailable: Boolean
}