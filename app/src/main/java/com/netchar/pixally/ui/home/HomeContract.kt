package com.netchar.pixally.ui.home

import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.domain.usecase.PhotosRequest
import com.netchar.pixally.ui.abstractions.viewmodel.UiEvent
import com.netchar.pixally.ui.abstractions.viewmodel.UiIntent
import com.netchar.pixally.ui.abstractions.viewmodel.UiState
import com.netchar.pixally.ui.home.adapter.UiImageItem

sealed class HomeIntent : UiIntent {
    object RequestPhotos : HomeIntent()
    class Refresh(val imageType: PhotosRequest.ImageType) : HomeIntent()
}

sealed class HomeEvent : UiEvent {
    object ShowLoadingIndicator : HomeEvent()
    class PhotosLoaded(val photos: List<Image>) : HomeEvent()
    class DisplayToastErrorMessage(val errorMessage: String) : HomeEvent()
}

data class HomeState(
    val isLoading: Boolean,
    val photos: List<UiImageItem>,
    val selectedImageType: PhotosRequest.ImageType,
    val errorMessage: ErrorMessage? = null
) : UiState {

    sealed class ErrorMessage {
        class Toast(val message: String) : ErrorMessage()
    }

    companion object {
        fun initial(): HomeState = HomeState(true, emptyList(), PhotosRequest.ImageType.ILLUSTRATION)
    }
}