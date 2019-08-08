package fargo.appsample.network

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
            .newBuilder()
            .addHeader("x-api-key", "DEMO-API-KEY")
            .build()
        return chain.proceed(req)
    }
}