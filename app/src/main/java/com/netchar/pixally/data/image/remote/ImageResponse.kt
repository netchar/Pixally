package com.netchar.pixally.data.image.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageResponse(
    val total: Int,
    val totalHits: Int,
    val hits: List<Image>
) {

    @Serializable
    data class Image(
        val id: Int,
        val pageURL: String,
        val type: String,
        val tags: String,
        val previewURL: String,
        val previewWidth: Int,
        val previewHeight: Int,
        val webformatURL: String,
        val webformatWidth: Int,
        val webformatHeight: Int,
        val largeImageURL: String,
        val imageWidth: Int,
        val imageHeight: Int,
        val imageSize: Int,
        val views: Int,
        val downloads: Int,
        val likes: Int,
        val comments: Int,

        @SerialName("user_id")
        val user_id: Int,

        val user: String,
        val userImageURL: String
    )
}

