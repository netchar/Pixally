package com.netchar.pixally.ui.home.adapter

import com.netchar.pixally.domain.entity.Image
import com.netchar.pixally.ui.abstractions.adapter.RecyclerItem

data class UiImageItem(
    val imageUrl: String
) : RecyclerItem.Model {

    companion object {
        fun Image.mapToUi(): UiImageItem {
            return UiImageItem(
                imageUrl = url
            )
        }
    }
}