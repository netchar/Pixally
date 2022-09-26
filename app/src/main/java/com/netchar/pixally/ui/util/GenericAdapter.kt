package com.netchar.pixally.ui.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.netchar.pixally.ui.abstractions.adapter.RecyclerItem
import com.netchar.pixally.ui.abstractions.adapter.BaseViewHolder

class GenericAdapter private constructor(
    private val recyclerItems: List<RecyclerItem<*, *>>,
) : ListAdapter<RecyclerItem.Model, BaseViewHolder<ViewBinding, RecyclerItem.Model>>(GenericDiffUtil(recyclerItems)) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewBinding, RecyclerItem.Model> {
        val inflater = LayoutInflater.from(parent.context)
        val adapterItem = recyclerItems.findItemOrThrow(viewType)
        return adapterItem.createViewHolder(inflater, parent) as BaseViewHolder<ViewBinding, RecyclerItem.Model>
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding, RecyclerItem.Model>, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        val adapterItem = recyclerItems.findItemOrThrow(item)
        return adapterItem.layoutId
    }

    private class GenericDiffUtil(
        private val recyclerItems: List<RecyclerItem<*, *>>,
    ) : DiffUtil.ItemCallback<RecyclerItem.Model>() {

        override fun areItemsTheSame(oldItem: RecyclerItem.Model, newItem: RecyclerItem.Model): Boolean {
            if (oldItem::class != newItem::class) return false

            return getItemCallback(oldItem).areItemsTheSame(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItem: RecyclerItem.Model, newItem: RecyclerItem.Model): Boolean {
            if (oldItem::class != newItem::class) return false

            return getItemCallback(oldItem).areContentsTheSame(oldItem, newItem)
        }

        private fun getItemCallback(item: RecyclerItem.Model): DiffUtil.ItemCallback<RecyclerItem.Model> {
            val adapterItem = recyclerItems.findItemOrThrow(item)
            return adapterItem.diffUtil as DiffUtil.ItemCallback<RecyclerItem.Model>
        }
    }

    companion object {
        private fun List<RecyclerItem<*, *>>.findItemOrThrow(item: RecyclerItem.Model): RecyclerItem<*,*> {
            return find { it.isRelativeItem(item) } ?: error("DiffUtil not found for $item")
        }

        private fun List<RecyclerItem<*, *>>.findItemOrThrow(viewType: Int): RecyclerItem<*,*> {
            return find { it.layoutId == viewType } ?: error("View type not found: $viewType")
        }

        fun create(vararg item: RecyclerItem<*, *>): GenericAdapter {
            return GenericAdapter(item.toList())
        }
    }
}