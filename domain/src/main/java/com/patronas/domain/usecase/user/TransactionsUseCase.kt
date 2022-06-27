package com.patronas.domain.usecase.user

import com.patronas.domain.model.UserBalanceModel

interface TransactionsUseCase {
    fun getBalance(): UserBalanceModel
    fun convertCurrency(fromCurrency: String, toCurrency: String, amount: Double)
}