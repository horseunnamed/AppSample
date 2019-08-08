package fargo.appsample.network

import fargo.appsample.entity.DogBreed
import fargo.appsample.entity.DogImage
import retrofit2.http.GET
import retrofit2.http.Query

interface DogsApi {

    @GET("images/search")
    suspend fun getImages(
        @Query("breed_id") breedId: String?,
        @Query("mime_types") mimeTypes: List<String>,
        @Query("order") order: DogImage.Order,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("size") size: String = "small"
    ): List<DogImage>

    @GET("breeds/search")
    suspend fun searchBreeds(
        @Query("q") query: String
    ): List<DogBreed>

}