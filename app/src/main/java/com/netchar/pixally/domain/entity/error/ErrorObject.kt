package com.netchar.pixally.domain.entity.error

sealed class ErrorObject {
    sealed class ApiError : ErrorObject() {
        object Network : ApiError()
        object AccessDenied : ApiError()
        object ServiceUnavailable : ApiError()
        object JsonParsing : ApiError()
        object NotFound : ApiError()
        object TooManyRequests : ApiError()
        object Unauthenticated : ApiError()
        object Timeout : ApiError()
        class Unknown(val code: Int, val message: String) : ApiError()
    }
}