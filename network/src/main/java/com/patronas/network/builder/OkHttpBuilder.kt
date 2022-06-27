package com.patronas.network.builder

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class OkHttpBuilder(
    private val connectTimeoutSeconds: Long,
    private val readTimeoutSeconds: Long,
    private val writeTimeoutSeconds: Long,
    private val interceptors: List<Interceptor?>?
) {

    fun makeOkHttpClient(): OkHttpClient {
        val okHttpBuilder = baseOkHttpClient.newBuilder()
            .connectTimeout(connectTimeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(readTimeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(writeTimeoutSeconds, TimeUnit.SECONDS)

        interceptors?.forEach { interceptor ->
            interceptor?.let {
                okHttpBuilder.addInterceptor(it)
            }
        }

        return okHttpBuilder.build()
    }

    companion object {
        var baseOkHttpClient: OkHttpClient = OkHttpClient()
    }

}