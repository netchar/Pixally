package com.netchar.pixally.ui.abstractions.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding

interface RecyclerItem<TBinding : ViewBinding, I : RecyclerItem.Model> {

    fun isRelativeItem(item: Model): Boolean

    @get:LayoutRes
    val layoutId: Int

    val diffUtil: DiffUtil.ItemCallback<I>

    fun createViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup): BaseViewHolder<TBinding, I>

    interface Model
}