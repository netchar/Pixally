package com.netchar.pixally.ui.home

import androidx.lifecycle.viewModelScope
import com.netchar.pixally.domain.entity.error.ErrorEntity
import com.netchar.pixally.domain.usecase.GetImagesUseCase
import com.netchar.pixally.infrastructure.ResultWrapper.Companion.onError
import com.netchar.pixally.infrastructure.ResultWrapper.Companion.onSuccess
import com.netchar.pixally.ui.abstractions.viewmodel.BaseMviViewModel
import com.netchar.pixally.ui.abstractions.viewmodel.StateReducer
import com.netchar.pixally.ui.home.adapter.UiImageItem.Companion.mapToUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getImages: GetImagesUseCase
) : BaseMviViewModel<HomeIntent, HomeState, HomeEvent>() {
    override fun createReducer(): StateReducer<HomeState, HomeEvent> = HomeStateReducer()

    init {
        sendIntent(HomeIntent.RequestPhotos)
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Refresh -> fetchPhotos()
            HomeIntent.RequestPhotos -> fetchPhotos()
        }
    }

    private fun fetchPhotos() {
        getImages.getImages()
            .onStart {
                emitEvent(HomeEvent.ShowLoadingIndicator)
            }.onEach {
                it.onSuccess {
                    emitEvent(HomeEvent.PhotosLoaded(data))
                }.onError {
                    when (error) {
                        is ErrorEntity.ApiError.Unknown -> emitEvent(HomeEvent.DisplayToastErrorMessage(error.message))
                        else -> emitEvent(HomeEvent.DisplayToastErrorMessage(error.toString()))
                    }
                }
            }.launchIn(viewModelScope)
    }

    private class HomeStateReducer : StateReducer<HomeState, HomeEvent>(HomeState.initial()) {
        override fun reduce(oldState: HomeState, event: HomeEvent) = when (event) {
            is HomeEvent.ShowLoadingIndicator -> oldState.copy(
                isLoading = true,
                errorMessage = null
            )
            is HomeEvent.PhotosLoaded -> oldState.copy(
                isLoading = false,
                photos = event.photos.map { it.mapToUi() },
                errorMessage = null
            )
            is HomeEvent.DisplayToastErrorMessage -> oldState.copy(
                isLoading = false,
                errorMessage = HomeState.ErrorMessage.Toast(event.errorMessage)
            )
        }
    }
}


