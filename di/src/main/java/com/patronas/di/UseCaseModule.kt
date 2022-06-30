package com.patronas.di

import com.patronas.data.repository.rates.RatesRepository
import com.patronas.domain.mapper.RatesDomainMapper
import com.patronas.domain.usecase.rates.GetRatesUseCase
import com.patronas.domain.usecase.rates.GetRatesUseCaseImpl
import com.patronas.domain.usecase.rates.transaction_fee.TransactionFeeUseCase
import com.patronas.domain.usecase.rates.transaction_fee.TransactionFeeUseCaseImpl
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

    @Provides
    @ViewModelScoped
    fun provideCalculateTransactionFee(): TransactionFeeUseCase {
        return TransactionFeeUseCaseImpl()
    }

}