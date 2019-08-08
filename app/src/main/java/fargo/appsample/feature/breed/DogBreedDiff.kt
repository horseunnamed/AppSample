package fargo.appsample.feature.breed

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import fargo.appsample.entity.DogBreed

class DogBreedDiff : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is DogBreed && newItem is DogBreed) {
            oldItem.id == newItem.id
        } else {
            false
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is DogBreed && newItem is DogBreed) {
            oldItem == newItem
        } else {
            false
        }
    }
}