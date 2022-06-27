package com.patronas.network.api

import com.patronas.network.config.NetworkConstants.API_KEY
import com.patronas.network.model.CurrencyRatesApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {

    @GET("fixer/latest")
    suspend fun getCurrencyRates(
        @Header("apiKey") apiKey: String = API_KEY
    ): Response<CurrencyRatesApiResponse>

}