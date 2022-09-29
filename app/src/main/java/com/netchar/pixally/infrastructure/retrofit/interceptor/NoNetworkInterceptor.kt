package com.netchar.pixally.infrastructure.retrofit.interceptor

import com.netchar.pixally.infrastructure.NetworkConnectivityChecker
import com.netchar.pixally.infrastructure.retrofit.exception.NoConnectivityException
import com.netchar.pixally.infrastructure.retrofit.exception.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NoNetworkInterceptor @Inject constructor(
    private val internetChecker: NetworkConnectivityChecker
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return when {
            !internetChecker.isConnectionOn -> throw NoConnectivityException()
            !internetChecker.isInternetAvailable -> throw NoInternetException()
            else -> chain.proceed(chain.request())
        }
    }
}