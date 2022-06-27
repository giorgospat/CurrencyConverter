package com.patronas.network.model

import com.patronas.network.model.base.BaseApiResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyRatesApiResponse(
    @Json(name = "timestamp")
    val timestamp: Long?,
    @Json(name = "base")
    val base: String?,
    @Json(name = "date")
    val date: String?,
    @Json(name = "rates")
    val rates: Map<String, Double>?
) : BaseApiResponse()