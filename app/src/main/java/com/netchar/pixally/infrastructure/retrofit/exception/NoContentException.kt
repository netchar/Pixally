package com.netchar.pixally.infrastructure.retrofit.exception

class NoContentException(
    val code: Int,
    override val message: String? =
        "The server has successfully fulfilled the request " +
                "with the code ($code) and that there is no additional content to send in the response payload body."
) : Throwable(message)