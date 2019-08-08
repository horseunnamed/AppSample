package fargo.appsample.core.ui.recycler.adapter

import androidx.recyclerview.widget.DiffUtil

class DefaultDiff<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = false
    override fun areContentsTheSame(oldItem: T, newItem: T) = false
}