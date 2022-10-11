package com.netchar.pixally.data.image.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.netchar.pixally.data.image.local.model.ImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Query("SELECT * FROM images ORDER BY id DESC")
    fun getImagesStream(): Flow<List<ImageEntity>>

    @Query("SELECT * FROM images WHERE imageType LIKE '%' || :imageType || '%' ORDER BY id DESC")
    fun getImagesByTypeStream(imageType: String): Flow<List<ImageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveImages(images: List<ImageEntity>)

    @Query("DELETE FROM images")
    suspend fun deleteAll()
}