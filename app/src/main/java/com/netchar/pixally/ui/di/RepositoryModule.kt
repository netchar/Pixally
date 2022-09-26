package com.netchar.pixally.ui.di

import com.netchar.pixally.data.photo.ImageRepositoryImpl
import com.netchar.pixally.domain.repo.ImageRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideImagesRepository(repositoryImpl: ImageRepositoryImpl): ImageRepository
}