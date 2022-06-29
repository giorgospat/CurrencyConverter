package com.patronas.storage.model

data class UserBalanceModel(
    var currencies: MutableMap<String, Double> = mutableMapOf()
)