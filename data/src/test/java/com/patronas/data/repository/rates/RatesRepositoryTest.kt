package com.patronas.data.repository.rates

import com.patronas.data.datasource.rates.RatesDataSource
import com.patronas.data.datasource.rates.RatesDataSourceImpl
import com.patronas.network.api.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class RatesRepositoryTest {

    @Mock
    private lateinit var apiService: ApiService

    private lateinit var repo: RatesRepository
    private lateinit var datasource: RatesDataSource

    @Before
    fun init() {
        MockitoAnnotations.openMocks(this)
        datasource = RatesDataSourceImpl(apiService = apiService)
        repo = RatesRepositoryImpl(datasource = datasource)
    }

    @Test
    fun `given getRates request on repository, verify retrofit service is called`() {
        runTest {
            repo.getRates()
            verify(apiService, times(1)).getCurrencyRates()
        }
    }

}
