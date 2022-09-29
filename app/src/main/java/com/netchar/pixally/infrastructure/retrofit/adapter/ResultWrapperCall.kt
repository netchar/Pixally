package com.netchar.pixally.infrastructure.retrofit.adapter

import com.netchar.pixally.domain.entity.error.ErrorEntity
import com.netchar.pixally.infrastructure.ResultWrapper
import com.netchar.pixally.infrastructure.retrofit.StatusCode
import com.netchar.pixally.infrastructure.retrofit.exception.NoConnectivityException
import com.netchar.pixally.infrastructure.retrofit.exception.NoContentException
import com.netchar.pixally.infrastructure.retrofit.exception.NoInternetException
import kotlinx.serialization.SerializationException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException

class ResultWrapperCall<T>(proxy: Call<T>) : ProxyCall<T, ResultWrapper<T>>(proxy) {
    override fun enqueueImpl(proxy: Call<T>, callback: Callback<ResultWrapper<T>>) {
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val result = parseSuccess(response)
                callback.onResponse(this@ResultWrapperCall, Response.success(result))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val result = parseError(t)
                callback.onResponse(this@ResultWrapperCall, Response.success(result))
            }
        })
    }

    private fun parseSuccess(response: Response<T>): ResultWrapper<T>? {
        val code = response.code()
        return try {
            if (response.isSuccessful) {
                val body = response.body() ?: throw NoContentException(code)
                ResultWrapper.Success(body)
            } else {
                ResultWrapper.Error(ErrorEntity.ApiError.Unknown(code, ""))
            }
        } catch (e: NoContentException) {
            parseError(e)
        } catch (e: Exception) {
            parseError(e)
        }
    }

    private fun parseError(ex: Throwable): ResultWrapper.Error {
        val apiError = when (ex) {
            is NoConnectivityException,
            is NoInternetException -> ErrorEntity.NoInternet
            is NoContentException -> ErrorEntity.ApiError.NoContent
            is SocketTimeoutException -> ErrorEntity.ApiError.Timeout
            is SerializationException -> ErrorEntity.ApiError.JsonParsing(ex.localizedMessage.orEmpty())
            is IOException -> ErrorEntity.ApiError.Network
            is HttpException -> when (val status = StatusCode.from(ex.code())) {
                StatusCode.TooManyRequests -> ErrorEntity.ApiError.TooManyRequests
                StatusCode.NotFound -> ErrorEntity.ApiError.NotFound
                StatusCode.Forbidden -> ErrorEntity.ApiError.AccessDenied
                StatusCode.ServiceUnavailable -> ErrorEntity.ApiError.ServiceUnavailable
                StatusCode.RequestTimeout -> ErrorEntity.ApiError.Timeout
                else -> ErrorEntity.ApiError.Unknown(status?.code ?: -1, ex.message())
            }
            else -> ErrorEntity.ApiError.Unknown(-1, ex.message.orEmpty())
        }.also {
            Timber.e(ex, "Error: $it")
        }
        return ResultWrapper.Error(apiError)
    }

    override fun cloneImpl(proxy: Call<T>) = ResultWrapperCall(proxy.clone())
}