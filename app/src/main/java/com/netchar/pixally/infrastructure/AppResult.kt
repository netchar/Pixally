package com.netchar.pixally.infrastructure

import com.netchar.pixally.domain.entity.error.ErrorEntity

sealed class AppResult<out T> {
    class Success<out T>(val data: T) : AppResult<T>()
    class Error(val error: ErrorEntity) : AppResult<Nothing>()

    companion object {
        inline fun <T> AppResult<T>.onSuccess(block: Success<T>.() -> Unit) = apply {
            if (this is Success) block(this)
        }

        inline fun <T> AppResult<T>.onError(block: Error.() -> Unit) = apply {
            if (this is Error) block(this)
        }
    }
}