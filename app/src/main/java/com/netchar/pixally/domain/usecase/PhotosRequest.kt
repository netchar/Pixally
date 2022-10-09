package com.netchar.pixally.domain.usecase

data class PhotosRequest(
    val page: Int,
    val perPage: Int,
    val imageType: ImageType,
    val safeSearch: Boolean
) {

    companion object {
        @JvmStatic
        fun default() = PhotosRequest(
            1,
            50,
            ImageType.PHOTO,
            true
        )

        fun by(imageType: ImageType) = PhotosRequest(
            1,
            5,
            imageType,
            true
        )
    }

    enum class ImageType(val value: String) {
        ALL("all"),
        PHOTO("photo"),
        ILLUSTRATION("illustration"),
        VECTOR("vector")
    }
}