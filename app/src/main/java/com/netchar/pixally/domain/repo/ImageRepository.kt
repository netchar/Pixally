package com.netchar.pixally.domain.repo

import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.domain.usecase.PhotosRequest
import com.netchar.pixally.infrastructure.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getImages(request: PhotosRequest): Flow<List<Image>>

    suspend fun refreshImages(request: PhotosRequest) : ResultWrapper<Unit>
}