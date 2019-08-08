package fargo.appsample.di.module

import fargo.appsample.di.provider.DogsApiProvider
import fargo.appsample.di.provider.HttpClientProvider
import fargo.appsample.network.DogsApi
import okhttp3.OkHttpClient
import toothpick.config.Module

class NetworkModule : Module() {
    init {
        bind(OkHttpClient::class.java).toProvider(HttpClientProvider::class.java).providesSingletonInScope()
        bind(DogsApi::class.java).toProvider(DogsApiProvider::class.java).providesSingletonInScope()
    }
}