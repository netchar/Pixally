package com.netchar.pixally.data.image.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.netchar.pixally.domain.usecase.PhotosRequest

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey
    val id: Int,
    val photoUrl: String,
    val name: String,
    val imageType: String
)