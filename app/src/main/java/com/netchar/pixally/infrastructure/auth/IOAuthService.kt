package com.example.playgroundapp.presentation.infrastructure.auth


interface IOAuthService {
    fun isAuthorized(): Boolean
    fun getUserApiAccessTokenKey(): String
}