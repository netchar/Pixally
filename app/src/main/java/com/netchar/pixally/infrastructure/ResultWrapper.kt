package com.netchar.pixally.infrastructure

import com.netchar.pixally.domain.entity.error.ErrorEntity
import kotlinx.coroutines.flow.FlowCollector

sealed class ResultWrapper<out T> {
    class Success<out T>(val data: T) : ResultWrapper<T>()
    class Error(val error: ErrorEntity) : ResultWrapper<Nothing>()

    companion object {
        inline fun <T> ResultWrapper<T>.onSuccess(block: Success<T>.() -> Unit) = apply {
            if (this is Success) block(this)
        }

        inline fun <T> ResultWrapper<T>.onError(block: Error.() -> Unit) = apply {
            if (this is Error) block(this)
        }

        suspend fun <T> FlowCollector<ResultWrapper<T>>.emitSuccess(data: T) {
            emit(Success(data))
        }

        suspend fun <T> FlowCollector<ResultWrapper<T>>.emitError(errorEntity: ErrorEntity) {
            emit(Error(errorEntity))
        }
    }
}