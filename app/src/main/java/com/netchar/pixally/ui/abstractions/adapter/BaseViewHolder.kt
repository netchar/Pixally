package com.netchar.pixally.ui.abstractions.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<out Binding : ViewBinding, M : RecyclerItem.Model>(
    protected val binding: Binding
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var model: M

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onItemClick(model)
            }
        }
    }

    fun bind(model: M) {
        this.model = model
        onBind(model)
    }

    protected abstract fun onBind(model: M)

    open fun onItemClick(model: M) = Unit
}