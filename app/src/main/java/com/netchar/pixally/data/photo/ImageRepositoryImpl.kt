package com.netchar.pixally.data.photo

import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.domain.entity.ImageDescription
import com.netchar.pixally.domain.repo.ImageRepository
import com.netchar.pixally.infrastructure.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor() : ImageRepository {

    override fun getPhotos(): Flow<Resource<List<Image>>> {
        return flow {
            delay(2000)
            emit(Resource.of {
                listOf<Image>(
                    Image(
                        url = "https://cdn.pixabay.com/user/2013/11/05/02-10-23-764_250x250.jpg",
                        isFavorite = false,
                        description = ImageDescription(
                            shortDescription = "Short Description",
                            longDescription = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                            authorName = "Autor Name",
                            sourceName = "Pixabay"
                        )
                    )
                )
            })
        }.flowOn(Dispatchers.IO)
    }
}