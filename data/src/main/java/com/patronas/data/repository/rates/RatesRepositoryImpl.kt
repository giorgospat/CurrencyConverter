package com.patronas.data.repository.rates

import com.patronas.data.base.DomainApiResult
import com.patronas.data.datasource.rates.RatesDataSource
import com.patronas.network.model.CurrencyRatesApiResponse

class RatesRepositoryImpl(
    private val datasource: RatesDataSource
) : RatesRepository {

    override suspend fun getRates(): DomainApiResult<CurrencyRatesApiResponse> {
        return when (val data = datasource.getRates()
        ) {
            is DomainApiResult.Success -> {
                DomainApiResult.Success(data.data)
            }
            is DomainApiResult.Error -> {
                DomainApiResult.Error()
            }
        }
    }

}