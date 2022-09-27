package com.netchar.pixally.data.image.remote

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ApiImageResponse(
        @SerialName("total") val total : Int,
        @SerialName("totalHits") val totalHits : Int,
        @SerialName("hits") val hits : List<ApiImage>
)

@kotlinx.serialization.Serializable
data class ApiImage(
        @SerialName("id") val id : Int,
        @SerialName("pageURL") val pageURL : String,
        @SerialName("type") val type : String,
        @SerialName("tags") val tags : String,
        @SerialName("previewURL") val previewURL : String,
        @SerialName("previewWidth") val previewWidth : Int,
        @SerialName("previewHeight") val previewHeight : Int,
        @SerialName("webformatURL") val webformatURL : String,
        @SerialName("webformatWidth") val webformatWidth : Int,
        @SerialName("webformatHeight") val webformatHeight : Int,
        @SerialName("largeImageURL") val largeImageURL : String,
        @SerialName("imageWidth") val imageWidth : Int,
        @SerialName("imageHeight") val imageHeight : Int,
        @SerialName("imageSize") val imageSize : Int,
        @SerialName("views") val views : Int,
        @SerialName("downloads") val downloads : Int,
        @SerialName("favorites") val favorites : Int,
        @SerialName("likes") val likes : Int,
        @SerialName("comments") val comments : Int,
        @SerialName("user_id") val user_id : Int,
        @SerialName("user") val user : String,
        @SerialName("userImageURL") val userImageURL : String
)