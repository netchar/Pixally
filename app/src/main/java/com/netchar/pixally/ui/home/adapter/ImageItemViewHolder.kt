package com.netchar.pixally.ui.home.adapter

import com.netchar.pixally.databinding.RowImageBinding
import com.netchar.pixally.ui.abstractions.adapter.BaseViewHolder
import com.netchar.pixally.ui.util.loadUrl

class ImageItemViewHolder(
    binding: RowImageBinding,
    private val onItemClick: (UiImageItem) -> Unit
) : BaseViewHolder<RowImageBinding, UiImageItem>(binding) {

    override fun onItemClick(model: UiImageItem) {
        onItemClick.invoke(model)
    }

    override fun onBind(model: UiImageItem) = with(binding) {
        homeItemImagePhoto.loadUrl(model.imageUrl)
    }
}