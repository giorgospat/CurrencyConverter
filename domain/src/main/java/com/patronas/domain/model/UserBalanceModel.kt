package com.patronas.domain.model

data class UserBalanceModel(
    var currencies: MutableMap<String, Double> = mutableMapOf()
)