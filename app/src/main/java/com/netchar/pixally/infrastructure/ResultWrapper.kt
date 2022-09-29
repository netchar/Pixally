package com.netchar.pixally.infrastructure

import com.netchar.pixally.domain.entity.error.ErrorEntity

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
    }
}