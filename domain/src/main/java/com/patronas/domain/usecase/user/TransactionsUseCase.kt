package com.patronas.domain.usecase.user

import com.patronas.domain.model.UserBalanceModel

interface TransactionsUseCase {
    fun initBalance(initialAmount: Double, primaryCurrency: String, currencies: List<String>)
    fun getBalance(): UserBalanceModel
    fun updateBalance(currency: String, amount: Double)
    fun convertCurrency(fromCurrency: String, toCurrency: String, amount: Double)
}