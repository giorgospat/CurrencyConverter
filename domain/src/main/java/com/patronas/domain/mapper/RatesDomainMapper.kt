package com.patronas.domain.mapper

import com.patronas.domain.model.RateModel
import com.patronas.domain.model.RatesDomainModel
import com.patronas.network.model.CurrencyRatesApiResponse

class RatesDomainMapper {

    fun mapToDomain(apiResponse: CurrencyRatesApiResponse): RatesDomainModel {

        return RatesDomainModel(
            baseRate = apiResponse.base ?: "",
            rates = apiResponse.rates?.map { RateModel(name = it.key, rate = it.value) } ?: listOf()
        )

    }

}