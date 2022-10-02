package com.netchar.pixally.infrastructure.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.netchar.pixally.data.image.local.dao.ImageDao
import com.netchar.pixally.data.image.local.model.ImageEntity

private const val DATABASE_VERSION = 1

@Database(
    entities = [ImageEntity::class],
    version = DATABASE_VERSION
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rocketDao(): ImageDao
}