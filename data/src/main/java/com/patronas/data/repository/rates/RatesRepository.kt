package com.patronas.data.repository.rates

import com.patronas.data.base.DomainApiResult
import com.patronas.network.model.CurrencyRatesApiResponse

interface RatesRepository {
    suspend fun getRates(): DomainApiResult<CurrencyRatesApiResponse>
}