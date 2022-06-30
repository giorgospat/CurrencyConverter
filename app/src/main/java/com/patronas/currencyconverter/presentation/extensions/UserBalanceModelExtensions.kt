package com.patronas.currencyconverter.presentation.extensions

import com.patronas.storage.model.UserBalanceModel

fun UserBalanceModel?.getBalanceFor(currency: String): Double {
    return this?.currencies?.get(currency) ?: 0.0
}