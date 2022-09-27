package com.netchar.pixally.domain.repo

import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.domain.usecase.GetImagesUseCase
import com.netchar.pixally.infrastructure.AppResult
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getImages(
        forceRefresh: Boolean,
        request: GetImagesUseCase.PhotosRequest
    ): Flow<ApiResponse<List<Image>>>
}