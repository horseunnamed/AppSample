package fargo.appsample.core.ui.recycler.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface AdapterDelegate<T> {
    fun isForItem(item: T): Boolean
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(item: T, viewHolder: RecyclerView.ViewHolder)
}