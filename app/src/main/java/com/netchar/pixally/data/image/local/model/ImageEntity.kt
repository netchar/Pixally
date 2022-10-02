package com.netchar.pixally.data.image.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey
    val id: Int,
    val photoUrl: String,
    val name: String,
)