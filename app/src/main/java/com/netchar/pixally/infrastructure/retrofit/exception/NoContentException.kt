package com.netchar.pixally.infrastructure.retrofit.exception

class NoContentException(
    code: Int
) : Throwable(
    "The server has successfully fulfilled the request " +
            "with the code ($code) and that there is no additional " +
            "content to send in the response payload body."
)