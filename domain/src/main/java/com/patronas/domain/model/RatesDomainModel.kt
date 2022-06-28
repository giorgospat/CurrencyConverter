package com.patronas.domain.model

import com.patronas.domain.model.reusable.RateModel

data class RatesDomainModel(
    val baseRate: String = "",
    val currencies: List<String> = listOf(),
    val rates: List<RateModel> = listOf()
)