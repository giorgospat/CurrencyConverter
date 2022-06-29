package com.patronas.currencyconverter.presentation.ui.screens.home

import androidx.lifecycle.viewModelScope
import com.patronas.currencyconverter.base.BaseViewModel
import com.patronas.currencyconverter.presentation.extensions.round
import com.patronas.data.base.DomainApiResult
import com.patronas.domain.EUR
import com.patronas.domain.USD
import com.patronas.domain.model.RatesDomainModel
import com.patronas.domain.usecase.GetRatesUseCase
import com.patronas.storage.datastore.transactions.TransactionsUseCase
import com.patronas.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val ratesUseCase: GetRatesUseCase,
    private val transactionsUseCase: TransactionsUseCase
) : BaseViewModel() {

    private val _ratesModel = MutableStateFlow(RatesDomainModel())
    private val ratesModel = _ratesModel.asStateFlow()

    private val _selectedSellCurrency = MutableStateFlow("")
    private val selectedSellCurrency = _selectedSellCurrency.asStateFlow()

    private val _sellCurrencies = MutableStateFlow(listOf(""))
    private val sellCurrencies = _sellCurrencies.asStateFlow()

    private val _selectedBuyCurrency = MutableStateFlow("")
    private val selectedBuyCurrency = _selectedBuyCurrency.asStateFlow()

    private val _buyCurrencies = MutableStateFlow(listOf(""))
    private val buyCurrencies = _buyCurrencies.asStateFlow()

    private val _sellAmount = MutableStateFlow("")
    private val sellAmount = _sellAmount.asStateFlow()

    private val _buyAmount = MutableStateFlow("")
    private val buyAmount = _buyAmount.asStateFlow()

    private val initialBalanceEUR = 1000.0

    init {
        viewModelScope.launch {
            withContext(dispatcher.background()) {
              //  fetchRates()
                setInitialTransactionCurrencies()
                setInitialBalances()
            }
        }
    }

    val uiState = HomeUiState(
        ratesModel = ratesModel,
        sellCurrencies = sellCurrencies,
        buyCurrencies = buyCurrencies,
        selectedSellCurrency = selectedSellCurrency,
        selectedBuyCurrency = selectedBuyCurrency,
        updateSellCurrency = {
            updateSellCurrencies(currency = it)
        },
        updateBuyCurrency = {
            updateBuyCurrencies(currency = it)
        },
        sellAmount = sellAmount,
        buyAmount = buyAmount,
        updateSellAmount = {
            _sellAmount.value = it.toString()
            _buyAmount.value = (it * 1.1).round()
            updateBalance(currency = selectedBuyCurrency.value, amount = it * 1.1)
        }
    )

    private suspend fun fetchRates() {
        when (val request = ratesUseCase.getRates()) {
            is DomainApiResult.Success -> {
                _ratesModel.value = request.data
            }
            is DomainApiResult.Error -> {
                //TODO handle error
            }
        }
    }

    private fun setInitialTransactionCurrencies() {
        _sellCurrencies.value = ratesModel.value.currencies.filter { it != USD }
        _buyCurrencies.value = ratesModel.value.currencies.filter { it != EUR }

        _selectedSellCurrency.value = ratesModel.value.currencies.find { it == EUR } ?: ""
        _selectedBuyCurrency.value = ratesModel.value.currencies.find { it == USD } ?: ""
    }

    private fun updateSellCurrencies(currency: String) {
        _selectedSellCurrency.value = currency
        //remove selected currency from buy list
        _buyCurrencies.value = ratesModel.value.currencies.toMutableList().filter { it != currency }
    }

    private fun updateBuyCurrencies(currency: String) {
        _selectedBuyCurrency.value = currency

        //remove selected currency from sell list
        _sellCurrencies.value =
            ratesModel.value.currencies.toMutableList().filter { it != currency }
    }

    private suspend fun setInitialBalances() {
        transactionsUseCase.initBalance(
            initialAmount = initialBalanceEUR,
            primaryCurrency = EUR,
            currencies = ratesModel.value.currencies
        )
    }

    private fun updateBalance(currency: String, amount: Double) {
        viewModelScope.launch(dispatcher.background()) {
            transactionsUseCase.updateBalance(currency = currency, amount = amount)
        }
    }

}