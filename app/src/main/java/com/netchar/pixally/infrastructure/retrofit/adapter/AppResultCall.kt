package com.netchar.pixally.infrastructure.retrofit.adapter

import com.netchar.pixally.domain.entity.error.ErrorEntity
import com.netchar.pixally.infrastructure.AppResult
import com.netchar.pixally.infrastructure.retrofit.StatusCode
import com.netchar.pixally.infrastructure.retrofit.exception.NoContentException
import kotlinx.serialization.SerializationException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class AppResultCall<T>(proxy: Call<T>) : ProxyCall<T, AppResult<T>>(proxy) {
    override fun enqueueImpl(proxy: Call<T>, callback: Callback<AppResult<T>>) {
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val result = parseSuccess(response)
                callback.onResponse(this@AppResultCall, Response.success(result))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val result = parseError(t)
                callback.onResponse(this@AppResultCall, Response.success(result))
            }
        })
    }

    private fun parseSuccess(response: Response<T>): AppResult<T>? {
        val code = response.code()
        return try {
            if (response.isSuccessful) {
                val body = response.body() ?: throw NoContentException(code)
                AppResult.Success(body)
            } else {
                AppResult.Error(ErrorEntity.ApiError.Unknown(code, ""))
            }
        } catch (e: NoContentException) {
            AppResult.Error(ErrorEntity.ApiError.Unknown(code, e.localizedMessage.orEmpty()))
        } catch (e: Exception) {
            AppResult.Error(ErrorEntity.ApiError.Unknown(code, e.localizedMessage.orEmpty()))
        }
    }

    private fun parseError(ex: Throwable): AppResult.Error {
        val apiError = when (ex) {
            is IOException -> ErrorEntity.ApiError.Network
            is SerializationException -> ErrorEntity.ApiError.JsonParsing(ex.localizedMessage.orEmpty())
            is HttpException -> when (val status = StatusCode.from(ex.code())) {
                StatusCode.TooManyRequests -> ErrorEntity.ApiError.TooManyRequests
                StatusCode.NotFound -> ErrorEntity.ApiError.NotFound
                StatusCode.Forbidden -> ErrorEntity.ApiError.AccessDenied
                StatusCode.ServiceUnavailable -> ErrorEntity.ApiError.ServiceUnavailable
                else -> ErrorEntity.ApiError.Unknown(status?.code ?: -1, ex.message())
            }
            else -> ErrorEntity.ApiError.Unknown(-1, ex.message.orEmpty())
        }.also {
            Timber.e(ex, "Error: $it")
        }
        return AppResult.Error(apiError)
    }

    override fun cloneImpl(proxy: Call<T>) = AppResultCall(proxy.clone())
}