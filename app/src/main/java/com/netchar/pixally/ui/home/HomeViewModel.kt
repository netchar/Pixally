package com.netchar.pixally.ui.home

import androidx.lifecycle.viewModelScope
import com.netchar.pixally.domain.entity.error.ErrorEntity
import com.netchar.pixally.domain.usecase.GetImagesUseCase
import com.netchar.pixally.domain.usecase.PhotosRequest
import com.netchar.pixally.domain.usecase.RefreshImagesUseCase
import com.netchar.pixally.infrastructure.ResultWrapper.Companion.onError
import com.netchar.pixally.infrastructure.ResultWrapper.Companion.onSuccess
import com.netchar.pixally.ui.abstractions.viewmodel.BaseMviViewModel
import com.netchar.pixally.ui.abstractions.viewmodel.StateReducer
import com.netchar.pixally.ui.home.adapter.UiImageItem.Companion.mapToUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getImages: GetImagesUseCase,
    private val refreshImages: RefreshImagesUseCase
) : BaseMviViewModel<HomeIntent, HomeState, HomeEvent>() {
    override fun createReducer(): StateReducer<HomeState, HomeEvent> = HomeStateReducer()

    private val filterByFlow = MutableStateFlow(state.value.selectedImageType)

    init {
        filterByFlow
            .onEach {
                emitEvent(HomeEvent.FilterApplied(it))
            }.flatMapLatest {
                getImages.getImages(PhotosRequest.by(it))
            }.onEach { resultWrapper ->
                resultWrapper
                    .onSuccess {
                        emitEvent(HomeEvent.PhotosLoaded(data))
                    }.onError {
                        emitEvent(HomeEvent.DisplayToastErrorMessage(error.toString()))
                    }
            }.launchIn(viewModelScope)
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.Refresh -> refreshImages()
            is HomeIntent.ApplyFilter -> filterPhotosBy(intent.imageType)
        }
    }

    private fun refreshImages() {
        viewModelScope.launch {
            emitEvent(HomeEvent.ShowLoadingIndicator)
            val photosRequest = PhotosRequest(
                page = Random.nextInt(2, 10),
                perPage = 5,
                imageType = state.value.selectedImageType,
                safeSearch = true
            )
            refreshImages.refreshImages(photosRequest)
                .onError {
                    handleErrors(error)
                }
        }
    }

    private fun filterPhotosBy(imageType: PhotosRequest.ImageType) {
        filterByFlow.tryEmit(imageType)
    }

    private fun handleErrors(error: ErrorEntity) {
        when (error) {
            is ErrorEntity.ApiError.Unknown -> emitEvent(HomeEvent.DisplayToastErrorMessage(error.message))
            else -> emitEvent(HomeEvent.DisplayToastErrorMessage(error.toString()))
        }
    }

    private class HomeStateReducer : StateReducer<HomeState, HomeEvent>(
        initialState = HomeState.initial()
    ) {
        override fun reduce(oldState: HomeState, event: HomeEvent): HomeState {
            return when (event) {
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
                is HomeEvent.FilterApplied -> oldState.copy(
                    isLoading = false,
                    errorMessage = null,
                    selectedImageType = event.imageType
                )
            }
        }
    }
}


