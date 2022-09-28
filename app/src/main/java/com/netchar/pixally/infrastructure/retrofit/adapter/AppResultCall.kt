package com.netchar.pixally.infrastructure.retrofit.adapter

import com.netchar.pixally.domain.entity.error.ErrorEntity
import com.netchar.pixally.infrastructure.AppResult
import com.netchar.pixally.infrastructure.retrofit.StatusCode
import com.netchar.pixally.infrastructure.retrofit.exception.NoContentException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class AppResultCall<T>(proxy: Call<T>) : ProxyCall<T, AppResult<T>>(proxy) {
    override fun enqueueImpl(proxy: Call<T>, callback: Callback<AppResult<T>>) {
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val code = response.code()
                val result = if (response.isSuccessful) {
                    // todo: check behavior
                    val body = response.body() ?: throw NoContentException(code)
                    AppResult.Success(body)
                } else {
                    AppResult.Error(ErrorEntity.ApiError.Unknown(code, ""))
                }

                callback.onResponse(this@AppResultCall, Response.success(result))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val result = AppResult.Error(parseError(t))
                callback.onResponse(this@AppResultCall, Response.success(result))
            }
        })
    }

    private fun parseError(ex: Throwable): ErrorEntity {
        return when (ex) {
            is IOException -> ErrorEntity.ApiError.Network
            is HttpException -> when (val status = StatusCode.from(ex.code())) {
                StatusCode.TooManyRequests -> ErrorEntity.ApiError.TooManyRequests
                StatusCode.NotFound -> ErrorEntity.ApiError.NotFound
                StatusCode.Forbidden -> ErrorEntity.ApiError.AccessDenied
                StatusCode.ServiceUnavailable -> ErrorEntity.ApiError.ServiceUnavailable
                else -> ErrorEntity.ApiError.Unknown(status?.code ?: -1, ex.message())
            }
            else -> ErrorEntity.ApiError.Unknown(-1, ex.message.orEmpty())
        }
    }

    override fun cloneImpl(proxy: Call<T>) = AppResultCall(proxy.clone())
}