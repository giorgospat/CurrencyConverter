package com.patronas.domain.usecase.rates.transaction_fee

import java.util.*

interface TransactionFeeUseCase {
    fun calculateFee(
        baseCurrencyRate: Double,
        currencyRate: Double,
        amount: Double,
        currentDate: Date,
        transactionHistory: List<Date>
    ): Double
}