package com.netchar.pixally.domain.repo

import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.domain.usecase.GetImagesUseCase
import com.netchar.pixally.infrastructure.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getImages(
        forceRefresh: Boolean,
        request: GetImagesUseCase.PhotosRequest
    ): Flow<ResultWrapper<List<Image>>>
}