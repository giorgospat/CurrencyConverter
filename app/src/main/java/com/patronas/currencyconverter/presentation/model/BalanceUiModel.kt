package com.patronas.currencyconverter.presentation.model

import com.patronas.currencyconverter.presentation.extensions.round
import com.patronas.storage.model.UserBalanceModel

data class BalanceUiModel(
    val currency: String,
    val amount: String
)

fun UserBalanceModel.toUiModel(firstCurrency: String): List<BalanceUiModel> {
    return this.currencies.toList()
        .map { BalanceUiModel(currency = it.first, amount = it.second.round()) }
        //keep base currency first in the list
        .sortedWith(compareBy<BalanceUiModel> { it.currency != firstCurrency }.thenBy { it.amount })
}