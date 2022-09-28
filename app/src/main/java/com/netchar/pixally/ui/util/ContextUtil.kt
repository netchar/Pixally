package com.netchar.pixally.ui.util

import android.content.Context
import android.widget.Toast
import com.netchar.pixally.BuildConfig

fun Context?.showToast(resourceId: Int) {
    this?.showToast(getString(resourceId))
}

fun Context?.showToast(message: CharSequence) {
    if (this != null) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

val isDebug get() = BuildConfig.DEBUG