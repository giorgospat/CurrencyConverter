package com.patronas.currencyconverter.presentation.ui.screens.home

import com.patronas.currencyconverter.presentation.model.BalanceUiModel
import com.patronas.domain.model.RatesDomainModel
import kotlinx.coroutines.flow.StateFlow

data class HomeUiState(
    val ratesModel: StateFlow<RatesDomainModel>,
    val sellCurrencies: StateFlow<List<String>>,
    val buyCurrencies: StateFlow<List<String>>,
    val selectedSellCurrency: StateFlow<String>,
    val selectedBuyCurrency: StateFlow<String>,
    val updateSellCurrency: (String) -> Unit,
    val updateBuyCurrency: (String) -> Unit,
    val makeTransaction: () -> Unit,
    val sellAmount: StateFlow<String>,
    val buyAmount: StateFlow<String>,
    val updateSellAmount: (String) -> Unit,
    val balances: StateFlow<List<BalanceUiModel>>,
    val transactionFee: StateFlow<Double>,
    val dismissDialog: () -> Unit,
    val onFeeLearnMore: () -> Unit
)

sealed class HomeUiEvent {
    data class ExchangeCompleted(val message: String) : HomeUiEvent()
    object InputAmountIncorrectError : HomeUiEvent()
    object InsufficientBalanceError : HomeUiEvent()
    object TransactionError : HomeUiEvent()
    data class FeesExplanation(val message: String) : HomeUiEvent()
    object LoadingRatesError : HomeUiEvent()
    object Default : HomeUiEvent()
}