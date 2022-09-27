package com.netchar.pixally.infrastructure.auth

import android.content.SharedPreferences
import com.example.playgroundapp.presentation.infrastructure.auth.IOAuthService
import com.netchar.pixally.BuildConfig

class OAuthService(
        private val oauthPrefs: SharedPreferences
) : IOAuthService {
    // todo: authorization logic
    private var isAuthorized = true

    override fun isAuthorized() = isAuthorized

    override fun getUserApiAccessTokenKey() = BuildConfig.DEBUG_API_ACCESS_KEY
}