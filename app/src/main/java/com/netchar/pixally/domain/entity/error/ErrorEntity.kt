package com.netchar.pixally.domain.entity.error

sealed class ErrorEntity {

    sealed class ApiError : ErrorEntity() {
        object Network : ApiError()
        object AccessDenied : ApiError()
        object ServiceUnavailable : ApiError()
        class JsonParsing(val message: String) : ApiError()
        object NotFound : ApiError()
        object TooManyRequests : ApiError()
        object Unauthenticated : ApiError()
        object Timeout : ApiError()
        object NoContent : ApiError()
        class Unknown(val code: Int, val message: String) : ApiError()
    }

    object NoInternet : ErrorEntity()
}