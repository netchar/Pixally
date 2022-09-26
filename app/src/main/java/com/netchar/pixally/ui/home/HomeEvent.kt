package com.netchar.pixally.ui.home

import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.ui.abstractions.viewmodel.UiEvent

sealed class HomeEvent : UiEvent {
    object ShowLoadingIndicator : HomeEvent()
    class PhotosLoaded(val photos: List<Image>) : HomeEvent()
    class DisplayToastErrorMessage(val errorMessage: String) : HomeEvent()
}