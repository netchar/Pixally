package com.netchar.pixally.infrastructure.retrofit.adapter

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class ProxyCall<TIn, TOut>(
    private val proxy: Call<TIn>
) : Call<TOut> {
    override fun execute(): Response<TOut> = throw NotImplementedError()
    final override fun enqueue(callback: Callback<TOut>) = enqueueImpl(proxy, callback)
    final override fun clone(): Call<TOut> = cloneImpl(proxy)

    override fun cancel() = proxy.cancel()
    override fun request(): Request = proxy.request()
    override fun isExecuted() = proxy.isExecuted
    override fun isCanceled() = proxy.isCanceled
    override fun timeout(): Timeout = proxy.timeout()

    abstract fun enqueueImpl(proxy: Call<TIn>, callback: Callback<TOut>)
    abstract fun cloneImpl(proxy: Call<TIn>): Call<TOut>
}