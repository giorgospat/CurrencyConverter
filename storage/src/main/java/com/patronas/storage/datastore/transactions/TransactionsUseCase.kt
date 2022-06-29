package com.patronas.storage.datastore.transactions

import com.patronas.storage.model.UserBalanceModel
import kotlinx.coroutines.flow.Flow

interface TransactionsUseCase {
    suspend fun initBalance(
        initialAmount: Double,
        primaryCurrency: String,
        currencies: List<String>
    )
    suspend fun getBalance(): Flow<UserBalanceModel>
    suspend fun updateBalance(currency: String, amount: Double)
}