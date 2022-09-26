package com.netchar.pixally.ui.abstractions.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow


abstract class BaseViewModel<TState : UiState, TEvent : UiEvent> : ViewModel() {
    private val reducer: StateReducer<TState, TEvent> by lazy {
        createReducer()
    }

    protected abstract fun createReducer(): StateReducer<TState, TEvent>

    val state: Flow<TState>
        get() = reducer.state

    fun emitEvent(event: TEvent) {
        reducer.acceptEvent(event)
    }
}

interface UiEvent
interface UiState
