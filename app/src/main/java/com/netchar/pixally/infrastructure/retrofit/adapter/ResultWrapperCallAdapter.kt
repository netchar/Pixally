package com.netchar.pixally.infrastructure.retrofit.adapter

import com.netchar.pixally.infrastructure.ResultWrapper
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ResultWrapperCallAdapter(
    private val type: Type
) : CallAdapter<Type, Call<ResultWrapper<Type>>> {
    override fun responseType(): Type {
        return type
    }

    override fun adapt(call: Call<Type>): Call<ResultWrapper<Type>> {
        return ResultWrapperCall(call)
    }
}