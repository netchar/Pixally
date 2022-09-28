package com.netchar.pixally.infrastructure.retrofit.adapter

import com.netchar.pixally.infrastructure.AppResult
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class AppResultAdapter(
    private val type: Type
) : CallAdapter<Type, Call<AppResult<Type>>> {
    override fun responseType(): Type {
        return type
    }

    override fun adapt(call: Call<Type>): Call<AppResult<Type>> {
        return AppResultCall(call)
    }
}