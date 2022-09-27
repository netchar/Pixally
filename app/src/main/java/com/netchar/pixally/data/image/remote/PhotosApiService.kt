package com.netchar.pixally.data.image.remote

import com.netchar.pixally.infrastructure.AppResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {

    @GET("api")
    suspend fun getImages(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("image_type") imageType: String,
        @Query("safesearch") safeSearch: Boolean
    ): AppResult<ApiImageResponse>
}