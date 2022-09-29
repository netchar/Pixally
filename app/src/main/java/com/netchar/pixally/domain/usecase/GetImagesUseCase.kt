package com.netchar.pixally.domain.usecase

import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.infrastructure.ResultWrapper
import kotlinx.coroutines.flow.Flow

fun interface GetImagesUseCase {
    fun getImages(forceRefresh: Boolean, request: PhotosRequest): Flow<ResultWrapper<List<Image>>>

    data class PhotosRequest(val page: Int = 1, val perPage: Int = 50, val imageType: ImageType = ImageType.PHOTO, val safeSearch: Boolean = true) {

        enum class ImageType(val value: String) {
            ALL("all"),
            PHOTO("photo"),
            ILLUSTRATION("illustration"),
            VECTOR("vector")
        }
    }
}