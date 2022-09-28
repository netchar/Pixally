package com.netchar.pixally.ui

import com.netchar.pixally.ui.util.isDebug
import timber.log.Timber

class AppInitializer {

    fun initialize() {
        initLogger()
    }

    private fun initLogger() {
        if (isDebug) {
            Timber.plant(Timber.DebugTree())
        }
    }
}