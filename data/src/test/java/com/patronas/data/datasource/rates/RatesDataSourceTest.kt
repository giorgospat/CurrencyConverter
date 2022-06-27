package com.patronas.data.datasource.rates

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
class RatesDataSourceTest {

    @Mock
    private lateinit var apiService: ApiService

    private lateinit var datasource: RatesDataSource

    @Before
    fun init() {
        MockitoAnnotations.openMocks(this)
        datasource = RatesDataSourceImpl(apiService = apiService)
    }

    @Test
    fun `given getRates request on datasource, verify retrofit service is called`() {
        runTest {
            datasource.getRates()
            verify(apiService, times(1)).getCurrencyRates()
        }
    }

}

