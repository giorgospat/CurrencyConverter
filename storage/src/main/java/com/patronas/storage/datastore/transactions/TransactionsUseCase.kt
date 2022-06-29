package com.patronas.storage.datastore.transactions

import com.patronas.storage.model.UserBalanceModel
import com.patronas.storage.model.transaction.TransactionResponse
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
        sellAmount: String,
        toCurrency: String,
        buyAmount: String
    ): TransactionResponse
}