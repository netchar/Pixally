package com.netchar.pixally.data.image.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.netchar.pixally.data.image.repository.ImageRepositoryImpl
import com.netchar.pixally.data.image.auth.AuthInterceptor
import com.netchar.pixally.data.image.auth.IOAuthService
import com.netchar.pixally.data.image.auth.OAuthService
import com.netchar.pixally.data.image.local.dao.ImageDao
import com.netchar.pixally.data.image.remote.ImageApi
import com.netchar.pixally.domain.repo.ImageRepository
import com.netchar.pixally.domain.usecase.GetImagesUseCase
import com.netchar.pixally.domain.usecase.RefreshImagesUseCase
import com.netchar.pixally.infrastructure.database.AppDatabase
import com.netchar.pixally.infrastructure.retrofit.adapter.ResultWrapperCallAdapterFactory
import com.netchar.pixally.infrastructure.retrofit.interceptor.NoNetworkInterceptor
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

    @Binds
    @Singleton
    abstract fun bindOauthService(impl: OAuthService) : IOAuthService

    companion object Provider {

        @Provides
        fun provideRefreshImagesUseCase(photosRepository: ImageRepository): RefreshImagesUseCase {
            return RefreshImagesUseCase(photosRepository::refreshImages)
        }

        @Provides
        fun providePhotosUseCase(photosRepository: ImageRepository): GetImagesUseCase {
            return GetImagesUseCase(photosRepository::getImages)
        }

        @Provides
        @Singleton
        fun provideImagesApi(retrofit: Retrofit): ImageApi {
            return retrofit.create(ImageApi::class.java)
        }

        @Provides
        @Singleton
        fun provideCharactersDao(database: AppDatabase) : ImageDao {
            return database.rocketDao()
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