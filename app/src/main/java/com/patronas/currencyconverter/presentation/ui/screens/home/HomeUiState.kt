package com.patronas.currencyconverter.presentation.ui.screens.home

import com.patronas.domain.model.RatesDomainModel
import com.patronas.currencyconverter.presentation.model.BalanceUiModel
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
    val balances: StateFlow<List<BalanceUiModel>>
)