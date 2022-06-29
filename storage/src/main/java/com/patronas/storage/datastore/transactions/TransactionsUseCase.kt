package com.patronas.storage.datastore.transactions

import com.patronas.storage.model.UserBalanceModel
import kotlinx.coroutines.flow.Flow

interface TransactionsUseCase {
    suspend fun initBalance(
        initialAmount: Double,
        primaryCurrency: String,
        currencies: List<String>
    )

    fun getBalance(): Flow<UserBalanceModel>
    suspend fun exchangeCurrency(
        fromCurrency: String,
        sellAmount: Double,
        toCurrency: String,
        buyAmount: Double
    )
}