package com.patronas.di

import com.patronas.data.datasource.rates.RatesDataSource
import com.patronas.data.repository.rates.RatesRepository
import com.patronas.data.repository.rates.RatesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideApiRepository(
        datasource: RatesDataSource
    ): RatesRepository {
        return RatesRepositoryImpl(
            datasource = datasource
        )
    }

}