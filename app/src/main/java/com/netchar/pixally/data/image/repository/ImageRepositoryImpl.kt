package com.netchar.pixally.data.image.repository

import com.netchar.pixally.data.image.local.dao.ImageDao
import com.netchar.pixally.data.image.local.model.ImageEntity
import com.netchar.pixally.data.image.mapper.ImageMapper.toDomains
import com.netchar.pixally.data.image.mapper.ImageMapper.toEntities
import com.netchar.pixally.data.image.remote.ImageApi
import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.domain.repo.ImageRepository
import com.netchar.pixally.domain.usecase.PhotosRequest
import com.netchar.pixally.infrastructure.CoroutineDispatcher
import com.netchar.pixally.infrastructure.ResultWrapper
import com.netchar.pixally.infrastructure.ResultWrapper.Companion.emitError
import com.netchar.pixally.infrastructure.ResultWrapper.Companion.emitSuccess
import com.netchar.pixally.infrastructure.ResultWrapper.Companion.onError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageApi: ImageApi,
    private val imageDao: ImageDao,
    private val dispatcher: CoroutineDispatcher
) : ImageRepository {

    override fun getImages(request: PhotosRequest): Flow<ResultWrapper<List<Image>>> {
        return imageDao.getImages()
            .transform { imageEntities: List<ImageEntity> ->
                if (imageEntities.isEmpty()) {
                    refreshImages(request)
                        .onError {
                            emitError { error }
                        }
                } else {
                    emitSuccess { imageEntities.toDomains() }
                }
            }
    }

    override suspend fun refreshImages(request: PhotosRequest): ResultWrapper<Unit> {
        return withContext(dispatcher.io) {
            when (val response = imageApi.getImages(request.page, request.perPage, request.imageType.value, request.safeSearch)) {
                is ResultWrapper.Success -> {
                    imageDao.saveImages(response.data.hits.toEntities())
                    ResultWrapper.Success(Unit)
                }
                is ResultWrapper.Error -> {
                    ResultWrapper.Error(response.error)
                }
            }
        }
    }
}