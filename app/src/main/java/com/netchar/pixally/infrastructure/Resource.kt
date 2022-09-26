package com.netchar.pixally.infrastructure

import com.netchar.pixally.domain.entity.error.ErrorEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import java.io.IOException

sealed class Resource<out T> {
    class Success<out T>(val data: T) : Resource<T>()
    class Error<out T>(val error: ErrorEntity) : Resource<T>()

    companion object {
        inline fun <R> of(block: () -> R): Resource<R> {
            return try {
                Success(block())
            } catch (t: TimeoutCancellationException) {
                Error(ErrorEntity.ApiError.Timeout)
            } catch (c: CancellationException) {
                throw c
            } catch (ex: Exception) {
                val errorEntity = when (ex) {
                    is IOException -> ErrorEntity.ApiError.Network
                    else -> ErrorEntity.ApiError.Unknown(-1, ex.message.orEmpty())
                }
                Error(errorEntity)
            }
        }
    }
}