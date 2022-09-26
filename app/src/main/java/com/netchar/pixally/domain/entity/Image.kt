package com.netchar.pixally.domain.entity

data class Image(
    val url: String,
    val isFavorite: Boolean,
    val description: ImageDescription
)