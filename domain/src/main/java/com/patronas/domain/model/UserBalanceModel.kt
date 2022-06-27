package com.patronas.domain.model

import com.patronas.domain.model.reusable.RateModel

data class UserBalanceModel(
    val eur: Double,
    val otherCurrencies: List<RateModel>
)