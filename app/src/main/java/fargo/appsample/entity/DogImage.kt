package fargo.appsample.entity

import com.squareup.moshi.Json

data class DogImage(
    val id: String,
    val url: String,
    val breeds: List<DogBreed>
) {
    enum class Type {
        STATIC, ANIMATED, ALL;
    }

    enum class Order {
        @Json(name = "ASC") ASC,
        @Json(name = "DESC") DESC,
        @Json(name = "RANDOM") RANDOM;
    }

}