package com.netchar.pixally.ui.util

import android.view.View
import android.widget.ImageView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager


fun ImageView.loadUrl(url: String) {
    Glide.with(context.applicationContext)
        .syncWithLifecycleOwner(this)
        .load(url)
        .into(this)
}

private fun RequestManager.syncWithLifecycleOwner(view: View): RequestManager {

    val syncRequest = object : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) = onStart()
        override fun onStop(owner: LifecycleOwner) = onStop()
        override fun onDestroy(owner: LifecycleOwner) {
            onDestroy()
            owner.lifecycle.removeObserver(this)
        }
    }

    view.findViewTreeLifecycleOwner()?.lifecycle?.addObserver(syncRequest)
    return this
}

