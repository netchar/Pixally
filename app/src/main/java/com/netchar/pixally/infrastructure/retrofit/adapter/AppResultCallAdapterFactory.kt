package com.netchar.pixally.infrastructure.retrofit.adapter

import com.netchar.pixally.infrastructure.AppResult
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class AppResultCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): AppResultAdapter? {
        if (returnType.isRawTypeOf<Call<*>>()) {
            val returnTypeParameter: Type = getGenericParameterOf(returnType)

            if (returnTypeParameter.isRawTypeOf<AppResult<*>>()) {
                val resultTypeParameter: Type = getGenericParameterOf(returnTypeParameter)
                return AppResultAdapter(resultTypeParameter)
            }
        }

        return null
    }

    private fun getGenericParameterOf(returnType: Type) = getParameterUpperBound(0, returnType as ParameterizedType)

    companion object {
        fun create() = AppResultCallAdapterFactory()

        private inline fun <reified T: Any> Type.isRawTypeOf() : Boolean {
            return getRawType(this) == T::class.java
        }
    }
}