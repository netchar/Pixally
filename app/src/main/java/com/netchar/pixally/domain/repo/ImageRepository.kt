package com.netchar.pixally.domain.repo

import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.infrastructure.Resource
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getPhotos(): Flow<Resource<List<Image>>>
}