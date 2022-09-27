package com.netchar.pixally.ui.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playgroundapp.presentation.infrastructure.auth.IOAuthService
import com.netchar.pixally.infrastructure.CoroutineDispatcher
import com.netchar.pixally.infrastructure.InternetChecker
import com.netchar.pixally.infrastructure.NetworkConnectivityChecker
import com.netchar.pixally.infrastructure.auth.AuthInterceptor
import com.netchar.pixally.infrastructure.auth.OAuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideDispatchers() : CoroutineDispatcher {
        return CoroutineDispatcher()
    }

    @Provides
    @Singleton
    fun provideInternetChecker(context: Context) : NetworkConnectivityChecker {
        return InternetChecker(context)
    }

    @Provides
    @Singleton
    fun provideOauthService(@AppPrefs oauthPrefs: SharedPreferences): IOAuthService = OAuthService(oauthPrefs)

    @Provides
    @Singleton
    @OAuthInterceptor
    fun provideOAuthInterceptor(oAuthRepository: IOAuthService): Interceptor = AuthInterceptor(oAuthRepository)

    @Provides
    @Singleton
    @AppPrefs
    fun provideAuthPrefs(app: Context): SharedPreferences = app.getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppPrefs