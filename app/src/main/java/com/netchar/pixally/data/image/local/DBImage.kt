package com.netchar.pixally.data.image.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class DBImage(
    @PrimaryKey
    val id: Int,
    val photoUrl: String,
    val name: String,
)