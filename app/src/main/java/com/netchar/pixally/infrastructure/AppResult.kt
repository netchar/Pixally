package com.netchar.pixally.infrastructure

import com.netchar.pixally.domain.entity.error.ErrorObject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import java.io.IOException

sealed class AppResult<out T> {
    class Success<out T>(val data: T) : AppResult<T>()
    class Error<out T>(val error: ErrorObject) : AppResult<T>()

    companion object {
        inline fun <R> of(block: () -> R): AppResult<R> {
            return try {
                Success(block())
            } catch (t: TimeoutCancellationException) {
                Error(ErrorObject.ApiError.Timeout)
            } catch (c: CancellationException) {
                throw c
            } catch (ex: Exception) {
                val errorEntity = when (ex) {
                    is IOException -> ErrorObject.ApiError.Network
                    else -> ErrorObject.ApiError.Unknown(-1, ex.message.orEmpty())
                }
                Error(errorEntity)
            }
        }
    }
}