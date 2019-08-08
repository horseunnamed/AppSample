package fargo.appsample.core.ui.recycler

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
fun <T> RecyclerView.submitList(list: List<T>?) {
    val adapter = (adapter as ListAdapter<T, *>)
    adapter.submitList(list)
}

