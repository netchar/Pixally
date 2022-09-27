package com.netchar.pixally.data.image

import com.netchar.pixally.data.image.remote.ApiImageResponse
import com.netchar.pixally.data.image.remote.ImageApi
import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.domain.entity.ImageDescription
import com.netchar.pixally.domain.entity.error.ErrorObject
import com.netchar.pixally.domain.repo.ImageRepository
import com.netchar.pixally.domain.usecase.GetImagesUseCase
import com.netchar.pixally.infrastructure.CoroutineDispatcher
import com.netchar.pixally.infrastructure.AppResult
import com.skydoves.sandwich.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageApi: ImageApi,
    private val dispatcher: CoroutineDispatcher
) : ImageRepository {

    override fun getImages(forceRefresh: Boolean, request: GetImagesUseCase.PhotosRequest): Flow<ApiResponse<List<Image>>> {
            return flow {
                imageApi.getImages(request.page, request.perPage, request.imageType.value, request.safeSearch)
                    .suspendOnSuccess {
                        emit(ApiResponse.Success(this))
                    }
            }
    }

    private object SuccessMapper : ApiSuccessModelMapper<ApiImageResponse, ApiResponse.Success<List<Image>>> {
        override fun map(apiErrorResponse: ApiResponse.Success<ApiImageResponse>): ApiResponse.Success<List<Image>> {
            val images = apiErrorResponse.data.hits.map {
                Image(
                    url = it.largeImageURL,
                    isFavorite = false,
                    description = ImageDescription("")
                )
            }
            return ApiResponse.Success(images)
        }
    }
}