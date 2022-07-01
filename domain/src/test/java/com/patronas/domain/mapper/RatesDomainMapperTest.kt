package com.patronas.domain.mapper

import com.patronas.domain.model.RatesDomainModel
import com.patronas.domain.model.reusable.RateModel
import com.patronas.network.model.CurrencyRatesApiResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class RatesDomainMapperTest {

    private val mapper = RatesDomainMapper()

    @Test
    fun `given rates api response, verify mapper returns correct photo url`() {
        val dummyApiResponse = CurrencyRatesApiResponse(
            timestamp = 1656355984,
            base = "EUR",
            date = "2022-06-27",
            rates = mapOf(Pair("AED", 3.886633), Pair("AFN", 94.698461))
        )

        val exceptedModel = RatesDomainModel(
            baseRate = "EUR",
            currencies = listOf("AED", "AFN"),
            rates = listOf(
                RateModel(name = "AED", rate = 3.886633),
                RateModel(name = "AFN", rate = 94.698461)
            )
        )

        assertEquals(mapper.mapToDomain(dummyApiResponse), exceptedModel)

    }

    @Test
    fun `given rates api response with null properties, verify mapper returns empty rates list`() {
        val dummyApiResponse = CurrencyRatesApiResponse(
            timestamp = 1656355984,
            base = "EUR",
            date = "2022-06-27",
            rates = null
        )

        val exceptedModel = RatesDomainModel(
            baseRate = "EUR",
            currencies = listOf(),
            rates = listOf()
        )

        assertEquals(mapper.mapToDomain(dummyApiResponse), exceptedModel)

    }

}