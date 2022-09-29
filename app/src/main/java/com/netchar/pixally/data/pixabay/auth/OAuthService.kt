package com.netchar.pixally.data.pixabay.auth

import android.content.SharedPreferences
import com.netchar.pixally.BuildConfig

class OAuthService(
        private val oauthPrefs: SharedPreferences
) : IOAuthService {
    // todo: authorization logic
    private var isAuthorized = true

    override fun isAuthorized() = isAuthorized

    override fun getUserApiAccessTokenKey() = BuildConfig.DEBUG_API_ACCESS_KEY
}