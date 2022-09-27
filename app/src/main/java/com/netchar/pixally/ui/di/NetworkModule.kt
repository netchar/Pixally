package com.netchar.pixally.ui.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.netchar.pixally.data.image.AppResultCallAdapterFactory
import com.netchar.pixally.data.image.remote.ImageApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    companion object {
        private const val BASE_URL = "https://pixabay.com/"
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @OAuthInterceptor authInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    @ExperimentalSerializationApi
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .addCallAdapterFactory(AppResultCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideImagesApi(retrofit: Retrofit) : ImageApi {
        return retrofit.create(ImageApi::class.java)
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class OAuthInterceptor

