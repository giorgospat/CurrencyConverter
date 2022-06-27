package com.patronas.domain.model

data class RatesDomainModel(
    val baseRate: String = "",
    val rates: List<RateModel> = listOf()
)

data class RateModel(
    val name: String = "",
    val rate: Double = 1.0
)