package com.netchar.pixally.domain.usecase

import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.infrastructure.ResultWrapper
import kotlinx.coroutines.flow.Flow

fun interface GetImagesUseCase {
    fun getImages(): Flow<ResultWrapper<List<Image>>>

    data class PhotosRequest(val page: Int, val perPage: Int, val imageType: ImageType, val safeSearch: Boolean) {

        companion object {
            @JvmStatic
            fun default() = PhotosRequest(1, 50, ImageType.PHOTO, true)
        }

        enum class ImageType(val value: String) {
            ALL("all"),
            PHOTO("photo"),
            ILLUSTRATION("illustration"),
            VECTOR("vector")
        }
    }
}