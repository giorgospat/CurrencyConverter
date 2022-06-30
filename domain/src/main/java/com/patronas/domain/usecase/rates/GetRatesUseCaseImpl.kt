package com.patronas.domain.usecase.rates

import com.patronas.data.base.DomainApiResult
import com.patronas.data.repository.rates.RatesRepository
import com.patronas.domain.mapper.RatesDomainMapper
import com.patronas.domain.model.RatesDomainModel

class GetRatesUseCaseImpl(
    private val repo: RatesRepository,
    private val mapper: RatesDomainMapper
) : GetRatesUseCase {

    override suspend fun getRates(): DomainApiResult<RatesDomainModel> {
        return when (val response = repo.getRates()) {
            is DomainApiResult.Success -> {
                DomainApiResult.Success(mapper.mapToDomain(response.data))
            }
            is DomainApiResult.Error -> {
                DomainApiResult.Error()
            }
        }
    }


}