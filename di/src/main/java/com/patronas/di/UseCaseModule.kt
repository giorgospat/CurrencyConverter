package com.patronas.di

import com.patronas.data.repository.rates.RatesRepository
import com.patronas.domain.mapper.RatesDomainMapper
import com.patronas.domain.usecase.GetRatesUseCase
import com.patronas.domain.usecase.GetRatesUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetRatesUseCase(
        repo: RatesRepository,
        mapper: RatesDomainMapper
    ): GetRatesUseCase {
        return GetRatesUseCaseImpl(repo, mapper)
    }

}