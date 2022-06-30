package com.patronas.domain.usecase.rates.transaction_fee

import com.patronas.domain.config.TransactionFeeConfiguration.baseFeePercent
import com.patronas.domain.config.TransactionFeeConfiguration.dailyLimitForBaseFee
import com.patronas.domain.config.TransactionFeeConfiguration.extraFeeAmount
import com.patronas.domain.config.TransactionFeeConfiguration.extraFeePercent
import com.patronas.domain.config.TransactionFeeConfiguration.freeExchanges
import com.patronas.domain.config.TransactionFeeConfiguration.zeroFee
import com.patronas.domain.extensions.day
import java.util.*

class TransactionFeeUseCaseImpl : TransactionFeeUseCase {

    override fun calculateFee(
        baseCurrencyRate: Double,
        currencyRate: Double,
        amount: Double,
        currentDate: Date,
        transactionHistory: List<Date>
    ): Double {
        return when {
            transactionHistory.size >= freeExchanges -> {
                val todayTransactionCount = transactionHistory.filter {
                    it.day() == currentDate.day()
                }.size

                if (todayTransactionCount <= dailyLimitForBaseFee) {
                    amount.div(currencyRate) * baseFeePercent
                } else {
                    val extraFeeInBaseCurrency = extraFeeAmount * baseCurrencyRate
                    amount.div(currencyRate) * extraFeePercent + extraFeeInBaseCurrency
                }
            }
            transactionHistory.size < freeExchanges -> {
                zeroFee
            }
            else -> {
                zeroFee
            }
        }
    }
}