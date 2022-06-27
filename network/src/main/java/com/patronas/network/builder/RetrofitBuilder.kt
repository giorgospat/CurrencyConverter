package com.patronas.network.builder


import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitBuilder {

    fun <T> makeRetrofitService(
        apiClass: Class<T>,
        baseUrl: String,
        connectTimeoutSeconds: Long,
        readTimeoutSeconds: Long,
        writeTimeoutSeconds: Long,
        interceptors: List<Interceptor?>?,
        moshi: Moshi
    ): T {

        val okHttpBuilder = OkHttpBuilder(
            connectTimeoutSeconds,
            readTimeoutSeconds,
            writeTimeoutSeconds,
            interceptors
        )

        val okHttpClient = okHttpBuilder.makeOkHttpClient()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(apiClass)

    }

}