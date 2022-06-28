package com.patronas.domain.usecase.user

import com.patronas.domain.model.UserBalanceModel

class TransactionsUseCaseImpl : TransactionsUseCase {

    private val userBalance = UserBalanceModel()

    override fun initBalance(
        initialAmount: Double,
        primaryCurrency: String,
        currencies: List<String>
    ) {
        userBalance.apply {
            this.currencies = currencies.associateWith { 0.0 }.toMutableMap()
            this.currencies[primaryCurrency] = initialAmount
        }
    }

    override fun getBalance(): UserBalanceModel {
        return userBalance
    }

    override fun updateBalance(currency: String, amount: Double) {
        userBalance.apply {
            this.currencies[currency] = amount
        }
    }

    override fun convertCurrency(fromCurrency: String, toCurrency: String, amount: Double) {

    }
}