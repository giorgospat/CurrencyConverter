package com.patronas.domain.extensions

import com.patronas.domain.model.RatesDomainModel

fun RatesDomainModel.getRateForCurrency(currency: String): Double {
    return this.rates.find { it.name == currency }?.rate ?: 1.0
}