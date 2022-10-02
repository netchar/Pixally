package com.netchar.pixally.ui.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.netchar.pixally.infrastructure.CoroutineDispatcher
import com.netchar.pixally.infrastructure.InternetChecker
import com.netchar.pixally.infrastructure.NetworkConnectivityChecker
import com.netchar.pixally.infrastructure.database.AppDatabase
import com.netchar.pixally.ui.AppInitializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideDispatchers(): CoroutineDispatcher {
        return CoroutineDispatcher()
    }

    @Provides
    @Singleton
    fun provideInternetChecker(@ApplicationContext context: Context): NetworkConnectivityChecker {
        return InternetChecker(context)
    }

    @Provides
    @Singleton
    @AppPrefs
    fun provideAuthPrefs(@ApplicationContext app: Context): SharedPreferences {
        return app.getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAppInitializer(): AppInitializer {
        return AppInitializer()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "pixally_db")
            .build()
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppPrefs