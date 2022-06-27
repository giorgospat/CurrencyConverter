package com.patronas.domain.usecase.user

import com.patronas.domain.model.UserBalanceModel

class TransactionsUseCaseImpl: TransactionsUseCase {
    override fun getBalance(): UserBalanceModel {
        TODO("Not yet implemented")
    }

    override fun convertCurrency(fromCurrency: String, toCurrency: String, amount: Double) {
        TODO("Not yet implemented")
    }
}