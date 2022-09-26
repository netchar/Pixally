package com.netchar.pixally.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.netchar.pixally.R
import com.netchar.pixally.databinding.RowImageBinding
import com.netchar.pixally.ui.abstractions.adapter.RecyclerItem
import com.netchar.pixally.ui.abstractions.adapter.BaseViewHolder

class ImageRecyclerItem(
    private val onItemClick: (UiImageItem) -> Unit
) : RecyclerItem<RowImageBinding, UiImageItem> {

    override fun isRelativeItem(item: RecyclerItem.Model): Boolean = item is UiImageItem

    override val layoutId: Int = R.layout.row_image

    override val diffUtil = object : DiffUtil.ItemCallback<UiImageItem>() {
        override fun areItemsTheSame(oldItem: UiImageItem, newItem: UiImageItem) = oldItem.imageUrl == newItem.imageUrl
        override fun areContentsTheSame(oldItem: UiImageItem, newItem: UiImageItem) = oldItem == newItem
    }

    override fun createViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup): BaseViewHolder<RowImageBinding, UiImageItem> {
        val binding = RowImageBinding.inflate(layoutInflater, parent, false)
        return ImageItemViewHolder(binding, onItemClick)
    }
}

