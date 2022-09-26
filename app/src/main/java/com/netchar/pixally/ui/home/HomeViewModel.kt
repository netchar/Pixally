package com.netchar.pixally.ui.home

import androidx.lifecycle.viewModelScope
import com.netchar.pixally.domain.usecase.GetImagesUseCase
import com.netchar.pixally.infrastructure.Resource
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
        getImages.invoke()
            .onStart {
                emitEvent(HomeEvent.ShowLoadingIndicator)
            }.onEach { result ->
                when (result) {
                    is Resource.Success -> emitEvent(HomeEvent.PhotosLoaded(result.data))
                    is Resource.Error -> emitEvent(HomeEvent.DisplayToastErrorMessage("Something went wrong"))
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