package com.netchar.pixally.data.image

import com.netchar.pixally.data.image.remote.ImageApi
import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.domain.entity.error.ErrorObject
import com.netchar.pixally.domain.repo.ImageRepository
import com.netchar.pixally.domain.usecase.GetImagesUseCase
import com.netchar.pixally.infrastructure.AppResult
import com.netchar.pixally.infrastructure.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.HttpURLConnection
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageApi: ImageApi,
    private val dispatcher: CoroutineDispatcher
) : ImageRepository {

    override fun getImages(forceRefresh: Boolean, request: GetImagesUseCase.PhotosRequest): Flow<AppResult<List<Image>>> {
        return flow<AppResult<List<Image>>> {
            imageApi.getImages(request.page, request.perPage, request.imageType.value, request.safeSearch)
                .onSuccess {
                    val result = this.data
                    val test = 0
                    val images = result.hits.map { Image(it.largeImageURL, false) }
                    emit(AppResult.Success(images))
                }
        }.flowOn(dispatcher.io)
    }
}

abstract class CallDelegate<TIn, TOut>(
    protected val proxy: Call<TIn>
) : Call<TOut> {
    override fun execute(): Response<TOut> = throw NotImplementedError()
    final override fun enqueue(callback: Callback<TOut>) = enqueueImpl(callback)
    final override fun clone(): Call<TOut> = cloneImpl()

    override fun cancel() = proxy.cancel()
    override fun request(): Request = proxy.request()
    override fun isExecuted() = proxy.isExecuted
    override fun isCanceled() = proxy.isCanceled
    override fun timeout(): Timeout = proxy.timeout()

    abstract fun enqueueImpl(callback: Callback<TOut>)
    abstract fun cloneImpl(): Call<TOut>
}

class AppResultCall<T>(proxy: Call<T>) : CallDelegate<T, AppResult<T>>(proxy) {
    companion object {
        private const val HTTP_TOO_MANY_REQUESTS = 429
    }

    override fun enqueueImpl(callback: Callback<AppResult<T>>) {
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val code = response.code()
                val result = if (response.isSuccessful) {
                    val body = response.body() ?: throw NoContentException(code)
                    AppResult.Success(body)
                } else {
                    AppResult.Error(ErrorObject.ApiError.Unknown(code, ""))
                }

                callback.onResponse(this@AppResultCall, Response.success(result))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val result = AppResult.Error(parseError(t))
                callback.onResponse(this@AppResultCall, Response.success(result))
            }
        })
    }

    private fun parseError(ex: Throwable): ErrorObject {
        return when (ex) {
            is IOException -> ErrorObject.ApiError.Network
            is HttpException -> when (ex.code()) {
                HTTP_TOO_MANY_REQUESTS -> ErrorObject.ApiError.TooManyRequests
                HttpURLConnection.HTTP_NOT_FOUND -> ErrorObject.ApiError.NotFound
                HttpURLConnection.HTTP_FORBIDDEN -> ErrorObject.ApiError.AccessDenied
                HttpURLConnection.HTTP_UNAVAILABLE -> ErrorObject.ApiError.ServiceUnavailable
                else -> ErrorObject.ApiError.Unknown(ex.code(), ex.message())
            }
            else -> ErrorObject.ApiError.Unknown(-1, ex.message.orEmpty())
        }
    }

    override fun cloneImpl() = AppResultCall(proxy.clone())
}

class AppResultAdapter(
    private val type: Type
) : CallAdapter<Type, Call<AppResult<Type>>> {
    override fun responseType() = type
    override fun adapt(call: Call<Type>): Call<AppResult<Type>> = AppResultCall(call)
}

class AppResultCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ) = when (getRawType(returnType)) {
        Call::class.java -> {
            val callType = getParameterUpperBound(0, returnType as ParameterizedType)
            when (getRawType(callType)) {
                Result::class.java -> {
                    val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                    AppResultAdapter(resultType)
                }
                else -> null
            }
        }
        else -> null
    }

    companion object {
        fun create() = AppResultCallAdapterFactory()
    }
}

class NoContentException(
    val code: Int,
    override val message: String? =
        "The server has successfully fulfilled the request " +
                "with the code ($code) and that there is no additional content to send in the response payload body."
) : Throwable(message)


@JvmSynthetic
inline fun <T> AppResult<T>.onSuccess(
    onResult: AppResult.Success<T>.() -> Unit
): AppResult<T> {
    if (this is AppResult.Success) {
        onResult(this)
    }
    return this
}

@JvmSynthetic
inline fun <T> AppResult<T>.onError(
    onResult: AppResult.Error.() -> Unit
): AppResult<T> {
    if (this is AppResult.Error) {
        onResult(this)
    }
    return this
}