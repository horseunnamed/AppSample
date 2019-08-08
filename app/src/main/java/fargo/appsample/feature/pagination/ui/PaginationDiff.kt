package fargo.appsample.feature.pagination.ui

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

open class PaginationDiff : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        if (oldItem is PageLoadingItem && newItem is PageLoadingItem) {
            return true
        } else if (oldItem is Throwable && newItem is Throwable) {
            return true
        }
        return false
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        if (oldItem is PageLoadingItem && newItem is PageLoadingItem) {
            return true
        } else if (oldItem is Throwable && newItem is Throwable) {
            return oldItem == newItem
        }
        return false
    }
}