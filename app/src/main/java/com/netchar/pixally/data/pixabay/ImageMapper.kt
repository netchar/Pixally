package com.netchar.pixally.data.pixabay

import com.netchar.pixally.data.pixabay.local.ImageEntity
import com.netchar.pixally.data.pixabay.remote.ImageResponse
import com.netchar.pixally.data.util.EntityMapper
import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.domain.entity.ImageDescription

object ImageMapper : EntityMapper<ImageResponse.Image, ImageEntity, Image> {

    override fun ImageResponse.Image.toEntity(): ImageEntity {
        return ImageEntity(
            id = id,
            photoUrl = previewURL,
            name = user
        )
    }

    override fun ImageEntity.toDomain(): Image {
        return Image(
            url = photoUrl,
            isFavorite = false,
            description = ImageDescription(name)
        )
    }
}