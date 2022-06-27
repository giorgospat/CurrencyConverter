package com.patronas.network.config

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkConfiguration(
    @Json(name = "baseUrl")
    val baseUrl: String = ""
)

