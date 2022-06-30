package com.patronas.domain.config

object TransactionFeeConfiguration {
    const val zeroFee = 0.0
    const val freeExchanges = 5 // first free transactions
    const val dailyLimitForBaseFee = 15 //transactions per day
    const val baseFee = 0.007 // percent of base currency
    const val extraFee = 0.012 // percent of base currency
    const val extraFeeAmount = 0.3 //amount of base currency

    const val baseFeePercent = baseFee * 100
    const val extraFeePercent = extraFee * 10
}