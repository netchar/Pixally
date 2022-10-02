package com.netchar.pixally.ui.abstractions.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


abstract class BaseMviViewModel<I: UiIntent, S : UiState, E : UiEvent> : ViewModel() {
    val state: StateFlow<S>
        get() = reducer.state

    private val reducer: StateReducer<S, E> by lazy {
        createReducer()
    }

    protected abstract fun createReducer(): StateReducer<S, E>

    fun sendIntent(intent: I) {
        onIntent(intent)
    }

    abstract fun onIntent(intent: I)

    protected fun emitEvent(event: E) {
        reducer.acceptEvent(event)
    }
}

interface UiEvent
interface UiState
interface UiIntent