package com.netchar.pixally.data.pixabay.di

import android.content.SharedPreferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.netchar.pixally.data.pixabay.ImageRepositoryImpl
import com.netchar.pixally.data.pixabay.auth.AuthInterceptor
import com.netchar.pixally.data.pixabay.auth.IOAuthService
import com.netchar.pixally.data.pixabay.auth.OAuthService
import com.netchar.pixally.data.pixabay.remote.ImageApi
import com.netchar.pixally.domain.repo.ImageRepository
import com.netchar.pixally.infrastructure.retrofit.adapter.ResultWrapperCallAdapterFactory
import com.netchar.pixally.infrastructure.retrofit.interceptor.NoNetworkInterceptor
import com.netchar.pixally.ui.di.AppPrefs
import com.netchar.pixally.ui.di.NetworkModule
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
abstract class ImageModule {

    @Binds
    abstract fun bindImagesRepository(repositoryImpl: ImageRepositoryImpl): ImageRepository

    companion object {

        @Provides
        @Singleton
        fun provideImagesApi(retrofit: Retrofit): ImageApi {
            return retrofit.create(ImageApi::class.java)
        }

        @Provides
        @Singleton
        fun provideOauthService(@AppPrefs oauthPrefs: SharedPreferences): IOAuthService {
            return OAuthService(oauthPrefs)
        }

        @Provides
        @Singleton
        fun provideOAuthInterceptor(oAuthRepository: IOAuthService): AuthInterceptor {
            return AuthInterceptor(oAuthRepository)
        }

        @Provides
        @Singleton
        @ExperimentalSerializationApi
        fun provideRetrofit(httpClient: OkHttpClient, json: Json): Retrofit {
            val contentType = "application/json".toMediaType()
            return Retrofit.Builder()
                .client(httpClient)
                .baseUrl("https://pixabay.com/")
                .addConverterFactory(json.asConverterFactory(contentType))
                .addCallAdapterFactory(ResultWrapperCallAdapterFactory.create())
                .build()
        }

        @Provides
        @Singleton
        fun provideOkHttpClient(
            okkHttpBuilder: OkHttpClient.Builder,
            authInterceptor: AuthInterceptor,
            loggingInterceptor: HttpLoggingInterceptor,
            networkInterceptor: NoNetworkInterceptor
        ): OkHttpClient {
            return okkHttpBuilder
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(networkInterceptor)
                .build()
        }
    }
}