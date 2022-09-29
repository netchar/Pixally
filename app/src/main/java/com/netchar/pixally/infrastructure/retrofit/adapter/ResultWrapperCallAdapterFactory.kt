package com.netchar.pixally.infrastructure.retrofit.adapter

import com.netchar.pixally.infrastructure.ResultWrapper
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultWrapperCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): ResultWrapperCallAdapter? {
        if (returnType.isRawTypeOf<Call<*>>()) {
            val returnTypeParameter: Type = getGenericParameterOf(returnType)

            if (returnTypeParameter.isRawTypeOf<ResultWrapper<*>>()) {
                val resultTypeParameter: Type = getGenericParameterOf(returnTypeParameter)
                return ResultWrapperCallAdapter(resultTypeParameter)
            }
        }

        return null
    }

    private fun getGenericParameterOf(returnType: Type) = getParameterUpperBound(0, returnType as ParameterizedType)

    companion object {
        fun create() = ResultWrapperCallAdapterFactory()

        private inline fun <reified T: Any> Type.isRawTypeOf() : Boolean {
            return getRawType(this) == T::class.java
        }
    }
}