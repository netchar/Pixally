package com.netchar.pixally.ui.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playgroundapp.presentation.infrastructure.auth.IOAuthService
import com.netchar.pixally.infrastructure.CoroutineDispatcher
import com.netchar.pixally.infrastructure.InternetChecker
import com.netchar.pixally.infrastructure.NetworkConnectivityChecker
import com.netchar.pixally.infrastructure.auth.AuthInterceptor
import com.netchar.pixally.infrastructure.auth.OAuthService
import com.netchar.pixally.ui.AppInitializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideOauthService(@AppPrefs oauthPrefs: SharedPreferences): IOAuthService {
        return OAuthService(oauthPrefs)
    }

    @Provides
    @Singleton
    @OAuthInterceptor
    fun provideOAuthInterceptor(oAuthRepository: IOAuthService): Interceptor {
        return AuthInterceptor(oAuthRepository)
    }

    @Provides
    @Singleton
    @AppPrefs
    fun provideAuthPrefs(@ApplicationContext app: Context): SharedPreferences {
        return app.getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAppInitializer() : AppInitializer {
        return AppInitializer()
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppPrefs