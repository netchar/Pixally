package com.netchar.pixally.data.image.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.netchar.pixally.data.image.local.model.ImageEntity
import com.netchar.pixally.domain.usecase.PhotosRequest
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Query("SELECT * FROM images WHERE imageType = :type ORDER BY id DESC")
    fun getImages(type: PhotosRequest.ImageType): Flow<List<ImageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveImages(images: List<ImageEntity>)
}