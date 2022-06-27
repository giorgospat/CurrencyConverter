package com.patronas.di

import com.patronas.data.datasource.rates.RatesDataSource
import com.patronas.data.datasource.rates.RatesDataSourceImpl
import com.patronas.network.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @Singleton
    fun provideRatesDataSource(
        apiService: ApiService
    ): RatesDataSource {
        return RatesDataSourceImpl(apiService = apiService)
    }

}