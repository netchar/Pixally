package com.netchar.pixally.ui.home

import androidx.lifecycle.viewModelScope
import com.netchar.pixally.domain.entity.error.ErrorObject
import com.netchar.pixally.domain.usecase.GetImagesUseCase
import com.netchar.pixally.infrastructure.AppResult
import com.netchar.pixally.ui.abstractions.viewmodel.BaseViewModel
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
) : BaseViewModel<HomeState, HomeEvent>() {
    override fun createReducer(): StateReducer<HomeState, HomeEvent> = HomeStateReducer()

    init {
        fetchPhotos()
    }

    private fun fetchPhotos() {
        getImages.getImages(false, GetImagesUseCase.PhotosRequest())
            .onStart {
                emitEvent(HomeEvent.ShowLoadingIndicator)
            }.onEach { result ->
                when (result) {
                    is AppResult.Success -> emitEvent(HomeEvent.PhotosLoaded(result.data))
                    is AppResult.Error -> when (result.error) {
                        ErrorObject.ApiError.AccessDenied,
                        ErrorObject.ApiError.JsonParsing,
                        ErrorObject.ApiError.Network,
                        ErrorObject.ApiError.NotFound,
                        ErrorObject.ApiError.ServiceUnavailable,
                        ErrorObject.ApiError.Timeout,
                        ErrorObject.ApiError.TooManyRequests,
                        ErrorObject.ApiError.Unauthenticated -> emitEvent(HomeEvent.DisplayToastErrorMessage(result.error.toString()))
                        is ErrorObject.ApiError.Unknown -> emitEvent(HomeEvent.DisplayToastErrorMessage(result.error.message))
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun refresh() {
        fetchPhotos()
    }

    private class HomeStateReducer : StateReducer<HomeState, HomeEvent>(HomeState.initial()) {
        override fun reduce(oldState: HomeState, event: HomeEvent): HomeState {
            return when (event) {
                is HomeEvent.ShowLoadingIndicator -> oldState.copy(
                    isLoading = true
                )
                is HomeEvent.PhotosLoaded -> oldState.copy(
                    isLoading = false,
                    photos = event.photos.map { it.mapToUi() }
                )
                is HomeEvent.DisplayToastErrorMessage -> oldState.copy(
                    isLoading = false,
                    errorMessage = HomeState.ErrorMessage.Toast(event.errorMessage)
                )
            }
        }
    }
}