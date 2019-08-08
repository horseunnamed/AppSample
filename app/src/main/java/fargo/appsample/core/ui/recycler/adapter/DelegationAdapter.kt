package fargo.appsample.core.ui.recycler.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class DelegationAdapter<T>(
    private val delegates: List<AdapterDelegate<T>>,
    diff: DiffUtil.ItemCallback<T> = DefaultDiff()
)
    : ListAdapter<T, RecyclerView.ViewHolder>(diff) {

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return delegates.indexOfFirst { it.isForItem(item) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates[viewType].onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegates[getItemViewType(position)].onBindViewHolder(getItem(position), holder)
    }
}