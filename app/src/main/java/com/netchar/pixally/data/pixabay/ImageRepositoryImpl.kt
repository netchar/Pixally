package com.netchar.pixally.data.pixabay

import com.netchar.pixally.data.pixabay.ImageMapper.toDomains
import com.netchar.pixally.data.pixabay.ImageMapper.toEntities
import com.netchar.pixally.data.pixabay.remote.ImageApi
import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.domain.repo.ImageRepository
import com.netchar.pixally.domain.usecase.GetImagesUseCase
import com.netchar.pixally.infrastructure.CoroutineDispatcher
import com.netchar.pixally.infrastructure.ResultWrapper
import com.netchar.pixally.infrastructure.ResultWrapper.Companion.onError
import com.netchar.pixally.infrastructure.ResultWrapper.Companion.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageApi: ImageApi,
    private val dispatcher: CoroutineDispatcher
) : ImageRepository {

    override fun getImages(forceRefresh: Boolean, request: GetImagesUseCase.PhotosRequest): Flow<ResultWrapper<List<Image>>> {
        return flow {
            imageApi.getImages(request.page, request.perPage, request.imageType.value, request.safeSearch)
                .onSuccess {
                    emit(ResultWrapper.Success(data.hits.toEntities().toDomains()))
                }.onError {
                    emit(this)
                }
        }.catch {
            Timber.e(it)
        }.flowOn(dispatcher.io)
    }
}