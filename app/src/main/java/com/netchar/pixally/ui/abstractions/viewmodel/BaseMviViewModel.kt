package com.netchar.pixally.ui.abstractions.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow


abstract class BaseMviViewModel<S : State, E : Event> : ViewModel() {
    val state: StateFlow<S>
        get() = reducer.state

    private val reducer: StateReducer<S, E> by lazy {
        createReducer()
    }

    protected abstract fun createReducer(): StateReducer<S, E>

    protected fun emitEvent(event: E) {
        reducer.acceptEvent(event)
    }
}

interface Event
interface State
