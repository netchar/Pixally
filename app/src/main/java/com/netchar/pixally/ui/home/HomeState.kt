package com.netchar.pixally.ui.home

import com.netchar.pixally.ui.abstractions.viewmodel.UiState
import com.netchar.pixally.ui.home.adapter.UiImageItem

data class HomeState(
    val isLoading: Boolean,
    val photos: List<UiImageItem>,
    val errorMessage: ErrorMessage? = null
) : UiState {

    sealed class ErrorMessage {
        class Toast(val message: String): ErrorMessage()
    }

    companion object {
        fun initial(): HomeState = HomeState(true, emptyList())
    }
}