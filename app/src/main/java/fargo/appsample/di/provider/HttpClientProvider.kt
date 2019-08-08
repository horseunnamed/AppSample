package fargo.appsample.di.provider

import android.content.Context
import fargo.appsample.BuildConfig
import fargo.appsample.network.TokenInterceptor
import io.palaima.debugdrawer.network.quality.NetworkQualityModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import javax.inject.Provider

class HttpClientProvider @Inject constructor(
    private val ctx: Context
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient {
        var builder = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply { level = HttpLoggingInterceptor.Level.BASIC })
            .addNetworkInterceptor(TokenInterceptor())

        if (BuildConfig.DEBUG) {
            builder = builder.addInterceptor(NetworkQualityModule.interceptor(ctx))
        }

        return builder.build()
    }

}