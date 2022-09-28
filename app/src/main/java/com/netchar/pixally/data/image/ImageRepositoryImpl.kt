package com.netchar.pixally.data.image

import com.netchar.pixally.data.image.remote.ImageApi
import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.domain.repo.ImageRepository
import com.netchar.pixally.domain.usecase.GetImagesUseCase
import com.netchar.pixally.infrastructure.AppResult
import com.netchar.pixally.infrastructure.AppResult.Companion.onSuccess
import com.netchar.pixally.infrastructure.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageApi: ImageApi,
    private val dispatcher: CoroutineDispatcher
) : ImageRepository {

    override fun getImages(forceRefresh: Boolean, request: GetImagesUseCase.PhotosRequest): Flow<AppResult<List<Image>>> {
        return flow<AppResult<List<Image>>> {

            Result
            imageApi.getImages(request.page, request.perPage, request.imageType.value, request.safeSearch)
                .onSuccess {
                    val result = this.data
                    val test = 0
                    val images = result.hits.map { Image(it.largeImageURL, false) }
                    emit(AppResult.Success(images))
                }
        }.flowOn(dispatcher.io)
    }
}