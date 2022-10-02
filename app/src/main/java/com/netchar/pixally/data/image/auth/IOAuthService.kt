package com.netchar.pixally.data.image.auth


interface IOAuthService {
    fun isAuthorized(): Boolean
    fun getUserApiAccessTokenKey(): String
}