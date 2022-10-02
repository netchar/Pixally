package com.netchar.pixally.domain.usecase

import com.netchar.pixally.infrastructure.ResultWrapper

fun interface RefreshImagesUseCase {
    suspend fun refreshImages(request: PhotosRequest) : ResultWrapper<Unit>
}