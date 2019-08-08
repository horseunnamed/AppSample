package fargo.appsample.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DogBreed(
    val id: String,
    val name: String
) : Parcelable