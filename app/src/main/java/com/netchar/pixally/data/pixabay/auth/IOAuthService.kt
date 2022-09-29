package com.netchar.pixally.data.pixabay.auth


interface IOAuthService {
    fun isAuthorized(): Boolean
    fun getUserApiAccessTokenKey(): String
}