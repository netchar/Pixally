package com.netchar.pixally.infrastructure.auth

import com.example.playgroundapp.presentation.infrastructure.auth.IOAuthService
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
        private val authService: IOAuthService
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request()
            .url
            .newBuilder()
            .addQueryParameter("key", authService.getUserApiAccessTokenKey())
            .build()

        val request: Request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}