package com.netchar.pixally.ui.abstractions.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding

class GenericRecyclerAdapter private constructor(
    private val adapterDelegate: RecyclerAdapterDelegate
) : ListAdapter<RecyclerItem.Model, BaseViewHolder<ViewBinding, RecyclerItem.Model>>(
    adapterDelegate.createDiffUtil()
) {
    override fun getItemViewType(position: Int): Int {
        return adapterDelegate.getItemViewType(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewBinding, RecyclerItem.Model> {
        return adapterDelegate.createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding, RecyclerItem.Model>, position: Int) {
        adapterDelegate.bindViewHolder(holder, getItem(position))
    }

    companion object {
        fun create(vararg item: RecyclerItem<*, *>): GenericRecyclerAdapter {
            val adapterDelegate = RecyclerAdapterDelegate(item.toList())
            return GenericRecyclerAdapter(adapterDelegate)
        }
    }
}