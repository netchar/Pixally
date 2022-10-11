package com.netchar.pixally.domain.usecase

import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.domain.repo.ImageRepository
import com.netchar.pixally.infrastructure.ResultWrapper
import kotlinx.coroutines.flow.Flow

fun interface GetImagesUseCase {
    fun getImages(request: PhotosRequest):  Flow<ResultWrapper<List<Image>>>
}

class GetImagesUseCaseImpl(
    private val repository: ImageRepository
) : GetImagesUseCase {

    override fun getImages(request: PhotosRequest): Flow<ResultWrapper<List<Image>>> {
        return repository.getImagesStream(request)
    }
}

