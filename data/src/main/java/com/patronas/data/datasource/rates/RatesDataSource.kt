package com.patronas.data.datasource.rates

import com.patronas.data.base.DomainApiResult
import com.patronas.network.model.CurrencyRatesApiResponse

interface RatesDataSource {
    suspend fun getRates(): DomainApiResult<CurrencyRatesApiResponse>
}