package com.netchar.pixally.domain.usecase

import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.infrastructure.Resource
import kotlinx.coroutines.flow.Flow

fun interface GetImagesUseCase : () -> Flow<Resource<List<Image>>>