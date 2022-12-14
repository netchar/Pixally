package com.netchar.pixally.ui.home

import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.domain.usecase.PhotosRequest
import com.netchar.pixally.ui.abstractions.viewmodel.Event
import com.netchar.pixally.ui.abstractions.viewmodel.State
import com.netchar.pixally.ui.home.adapter.UiImageItem


sealed class HomeEvent : Event {
    object ShowLoadingIndicator : HomeEvent()
    class FilterApplied(val imageType: PhotosRequest.ImageType): HomeEvent()
    class PhotosLoaded(val photos: List<Image>) : HomeEvent()
    class DisplayToastErrorMessage(val errorMessage: String) : HomeEvent()
}

data class HomeState(
    val isLoading: Boolean,
    val photos: List<UiImageItem>,
    val selectedImageType: PhotosRequest.ImageType,
    val errorMessage: ErrorMessage? = null
) : State {

    sealed class ErrorMessage {
        class Toast(val message: String) : ErrorMessage()
    }

    companion object {
        fun initial(): HomeState = HomeState(true, emptyList(), PhotosRequest.ImageType.PHOTO)
    }
}