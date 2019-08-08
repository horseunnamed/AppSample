package fargo.appsample.network

import fargo.appsample.entity.DogImage

fun DogImage.Type.toMimeTypes() =
    when (this) {
        DogImage.Type.STATIC -> listOf("jpg", "png")
        DogImage.Type.ANIMATED -> listOf("gif")
        DogImage.Type.ALL -> emptyList()
    }
