package com.patronas.domain.usecase.rates.transaction_fee

import com.patronas.domain.config.TransactionFeeConfiguration.zeroFee
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.*

class TransactionFeeUseCaseTest {

    private val feeUseCase: TransactionFeeUseCaseImpl = TransactionFeeUseCaseImpl()
    private val cal = Calendar.getInstance()

    private val baseCurrencyRate = 1.0
    private val currencyRate = 3.886633
    private val amountToSell = 100.0

    @Test
    fun `given total transaction are less than free limit, verify fee is zero`() {
        val calculatedFee = feeUseCase.calculateFee(
            baseCurrencyRate = baseCurrencyRate,
            currencyRate = currencyRate,
            amount = amountToSell,
            currentDate = currentDate(),
            transactionHistory = dummyTransactionDates()
        )
        val expectedFee = zeroFee

        assertEquals(calculatedFee, expectedFee)
    }

    @Test
    fun `given total transaction are more than free limit and less than base fee daily limit, verify BASE fee is added`() {
        val calculatedFee = feeUseCase.calculateFee(
            baseCurrencyRate = baseCurrencyRate,
            currencyRate = currencyRate,
            amount = amountToSell,
            currentDate = currentDate(),
            transactionHistory = get8Transactions()
        )
        val expectedFee = 0.18010447603362603

        assertEquals(calculatedFee, expectedFee)
    }

    @Test
    fun `given total transaction are more than free limit and MORE than base fee daily limit, verify EXTRA fee is added`() {
        val calculatedFee = feeUseCase.calculateFee(
            baseCurrencyRate = baseCurrencyRate,
            currencyRate = currencyRate,
            amount = amountToSell,
            currentDate = currentDate(),
            transactionHistory = get16Transactions()
        )
        val expectedFee = 0.6087505303433589

        assertEquals(calculatedFee, expectedFee)
    }

    /**
     * Helper function
     */


    private fun dummyTransactionDates(): List<Date> {
        val transaction1 = cal.time.time
        val transaction2 = cal.time.time + 1000
        val transaction3 = cal.time.time + 2000
        val transaction4 = cal.time.time + 3000

        return listOf(
            Date(transaction1),
            Date(transaction2),
            Date(transaction3),
            Date(transaction4),
        )
    }

    private fun get8Transactions(): List<Date> {
        return (0 until 2).flatMap { dummyTransactionDates() }
    }

    private fun get16Transactions(): List<Date> {
        return (0 until 4).flatMap { dummyTransactionDates() }
    }

    private fun currentDate(): Date {
        return cal.time
    }

}