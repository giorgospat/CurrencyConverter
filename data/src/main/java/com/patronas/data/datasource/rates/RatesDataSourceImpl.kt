package com.patronas.data.datasource.rates

import com.patronas.data.base.DomainApiResult
import com.patronas.data.datasource.base.BaseDatasource
import com.patronas.network.api.ApiService
import com.patronas.network.model.CurrencyRatesApiResponse

class RatesDataSourceImpl(
    private val apiService: ApiService
) : RatesDataSource,
    BaseDatasource() {
    override suspend fun getRates(): DomainApiResult<CurrencyRatesApiResponse> {
        return try {
            execute(
                apiService.getCurrencyRates()
            )
        } catch (e: Throwable) {
            DomainApiResult.Error()
        }
    }

}