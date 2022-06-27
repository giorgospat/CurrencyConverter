package com.patronas.di

import com.patronas.network.api.ApiService
import com.patronas.network.builder.RetrofitBuilder
import com.patronas.network.config.NetworkConfiguration
import com.patronas.network.config.NetworkConstants
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor? {
        return if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            null
        }
    }

    @Provides
    @Singleton
    fun provideNetworkConfiguration(): NetworkConfiguration {
        return if (BuildConfig.SERVICE_CONFIG_LIVE) {
            NetworkConstants.getLiveConfiguration()
        } else {
            NetworkConstants.getTestConfiguration()
        }
    }

    @Provides
    @Singleton
    fun provideApiService(
        moshi: Moshi,
        networkConfiguration: NetworkConfiguration,
        httpLoggingInterceptor: HttpLoggingInterceptor?
    ): ApiService {
        return RetrofitBuilder().makeRetrofitService(
            apiClass = ApiService::class.java,
            baseUrl = networkConfiguration.baseUrl,
            connectTimeoutSeconds = NetworkConstants.connectTimeoutSeconds,
            readTimeoutSeconds = NetworkConstants.readTimeoutSeconds,
            writeTimeoutSeconds = NetworkConstants.writeTimeoutSeconds,
            interceptors = listOf(httpLoggingInterceptor),
            moshi = moshi
        )
    }

}