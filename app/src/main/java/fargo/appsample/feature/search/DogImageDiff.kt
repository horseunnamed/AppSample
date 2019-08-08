package fargo.appsample.feature.search

import android.annotation.SuppressLint
import fargo.appsample.entity.DogImage
import fargo.appsample.feature.pagination.ui.PaginationDiff

class DogImageDiff : PaginationDiff() {

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        if (oldItem is DogImage && newItem is DogImage) {
            return oldItem.id == newItem.id
        }
        return super.areItemsTheSame(oldItem, newItem)
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        if (oldItem is DogImage && newItem is DogImage) {
            return oldItem == newItem
        }
        return super.areItemsTheSame(oldItem, newItem)
    }

}