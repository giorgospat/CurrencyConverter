package com.patronas.domain.usecase.rates

import com.patronas.data.base.DomainApiResult
import com.patronas.domain.model.RatesDomainModel

interface GetRatesUseCase {
    suspend fun getRates(): DomainApiResult<RatesDomainModel>
}