package com.patronas.domain.mapper

import com.patronas.domain.model.RatesDomainModel
import com.patronas.domain.model.reusable.RateModel
import com.patronas.network.model.CurrencyRatesApiResponse

class RatesDomainMapper {

    fun mapToDomain(apiResponse: CurrencyRatesApiResponse): RatesDomainModel {

        return RatesDomainModel(
            baseRate = apiResponse.base ?: "",
            currencies = apiResponse.rates?.map { it.key } ?: listOf(),
            rates = apiResponse.rates?.map { RateModel(name = it.key, rate = it.value) } ?: listOf()
        )

    }

}