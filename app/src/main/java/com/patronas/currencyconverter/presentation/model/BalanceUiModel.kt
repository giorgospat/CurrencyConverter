package com.patronas.currencyconverter.presentation.model

import com.patronas.currencyconverter.presentation.extensions.round
import com.patronas.storage.model.UserBalanceModel

data class BalanceUiModel(
    val currency: String,
    val amount: String
)

fun UserBalanceModel.toUiModel(): List<BalanceUiModel> {
    return this.currencies.toList()
        .map { BalanceUiModel(currency = it.first, amount = it.second.round()) }
        .sortedBy { it.currency }
}