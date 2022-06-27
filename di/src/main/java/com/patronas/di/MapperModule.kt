package com.patronas.di

import com.patronas.domain.mapper.RatesDomainMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MapperModule {

    @Provides
    @Singleton
    fun provideRatesDomainMapper(): RatesDomainMapper {
        return RatesDomainMapper()
    }
}