package com.netchar.pixally.ui.abstractions.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class StateReducer<State : com.netchar.pixally.ui.abstractions.viewmodel.State, in Event : com.netchar.pixally.ui.abstractions.viewmodel.Event>(
    initialState: State
) {
    private val mutableState: MutableStateFlow<State> = MutableStateFlow(initialState)

    val state: StateFlow<State> get() = mutableState

    fun acceptEvent(event: Event) {
        val newState = reduce(mutableState.value, event)
        mutableState.tryEmit(newState)
    }

    abstract fun reduce(oldState: State, event: Event): State
}