package com.netchar.pixally.data.image

import com.netchar.pixally.data.image.remote.ImageApi
import com.netchar.pixally.data.util.EntityMapper
import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.domain.repo.ImageRepository
import com.netchar.pixally.domain.usecase.GetImagesUseCase
import com.netchar.pixally.infrastructure.AppResult
import com.netchar.pixally.infrastructure.AppResult.Companion.onError
import com.netchar.pixally.infrastructure.AppResult.Companion.onSuccess
import com.netchar.pixally.infrastructure.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageApi: ImageApi,
    private val dispatcher: CoroutineDispatcher
) : ImageRepository {

    override fun getImages(forceRefresh: Boolean, request: GetImagesUseCase.PhotosRequest): Flow<AppResult<List<Image>>> {
        return flow {
            imageApi.getImages(request.page, request.perPage, request.imageType.value, request.safeSearch)
                .onSuccess {
                    val result = this.data
                    val images = result.hits.map { Image(it.largeImageURL, false) }
                    emit(AppResult.Success(images))
                }.onError {
                    emit(this)
                }
        }.catch {
            val t = 0
        }.flowOn(dispatcher.io)
    }
}