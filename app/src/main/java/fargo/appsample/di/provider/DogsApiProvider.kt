package fargo.appsample.di.provider

import fargo.appsample.network.DogsApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Provider

class DogsApiProvider @Inject constructor(
    private val httpClient: OkHttpClient
) : Provider<DogsApi> {

    override fun get(): DogsApi {
        return Retrofit.Builder()
            .baseUrl("https://api.thedogapi.com/v1/")
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(DogsApi::class.java)
    }

}