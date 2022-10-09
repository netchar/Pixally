package com.netchar.pixally.ui.di

import com.netchar.pixally.infrastructure.NetworkConnectivityChecker
import com.netchar.pixally.infrastructure.retrofit.interceptor.NoNetworkInterceptor
import com.netchar.pixally.ui.util.isDebug
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        networkInterceptor: NoNetworkInterceptor
    ): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(networkInterceptor)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .apply {
                if (isDebug) {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    setLevel(HttpLoggingInterceptor.Level.NONE)
                }
            }
    }

    @Provides
    @Singleton
    fun provideNoNetworkInterceptor(internetChecker: NetworkConnectivityChecker): NoNetworkInterceptor {
        return NoNetworkInterceptor(internetChecker)
    }

    @Provides
    @Singleton
    @ExperimentalSerializationApi
    fun provideSerializationJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            prettyPrintIndent = "  "
        }
    }
}