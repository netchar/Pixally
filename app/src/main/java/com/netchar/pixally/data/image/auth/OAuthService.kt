package com.netchar.pixally.data.image.auth

import android.content.SharedPreferences
import com.netchar.pixally.BuildConfig
import com.netchar.pixally.ui.di.AppPrefs
import javax.inject.Inject

class OAuthService @Inject constructor(
    @AppPrefs private val oauthPrefs: SharedPreferences
) : IOAuthService {
    // todo: authorization logic
    private var isAuthorized = true

    override fun isAuthorized() = isAuthorized

    override fun getUserApiAccessTokenKey() = BuildConfig.DEBUG_API_ACCESS_KEY
}