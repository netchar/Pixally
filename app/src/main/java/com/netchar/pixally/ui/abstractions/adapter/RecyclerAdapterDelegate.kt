package com.netchar.pixally.ui.abstractions.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding

@Suppress("unchecked_cast")
class RecyclerAdapterDelegate(
    private val recyclerItems: List<RecyclerItem<*, *>>
) {
    fun createViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewBinding, RecyclerItem.Model> {
        val inflater = LayoutInflater.from(parent.context)
        val adapterItem = recyclerItems.findItemOrThrow(viewType)
        return adapterItem.createViewHolder(inflater, parent) as BaseViewHolder<ViewBinding, RecyclerItem.Model>
    }

    fun bindViewHolder(holder: BaseViewHolder<ViewBinding, RecyclerItem.Model>, item: RecyclerItem.Model) {
        holder.bind(item)
    }

    fun getItemViewType(item: RecyclerItem.Model): Int {
        val adapterItem = recyclerItems.findItemOrThrow(item)
        return adapterItem.layoutId
    }

    fun createDiffUtil() : DiffUtil.ItemCallback<RecyclerItem.Model> {
        return object  : DiffUtil.ItemCallback<RecyclerItem.Model>() {
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
    }

    companion object {
        private fun List<RecyclerItem<*, *>>.findItemOrThrow(item: RecyclerItem.Model): RecyclerItem<*,*> {
            return find { it.isRelativeItem(item) } ?: error("DiffUtil not found for $item")
        }

        private fun List<RecyclerItem<*, *>>.findItemOrThrow(viewType: Int): RecyclerItem<*,*> {
            return find { it.layoutId == viewType } ?: error("View type not found: $viewType")
        }
    }
}

