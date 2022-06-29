package com.patronas.currencyconverter.presentation.ui.screens.home

import androidx.lifecycle.viewModelScope
import com.patronas.currencyconverter.base.BaseViewModel
import com.patronas.currencyconverter.presentation.extensions.round
import com.patronas.currencyconverter.presentation.model.BalanceUiModel
import com.patronas.currencyconverter.presentation.model.toUiModel
import com.patronas.data.base.DomainApiResult
import com.patronas.domain.EUR
import com.patronas.domain.USD
import com.patronas.domain.extensions.getRateForCurrency
import com.patronas.domain.model.RatesDomainModel
import com.patronas.domain.model.reusable.RateModel
import com.patronas.domain.usecase.GetRatesUseCase
import com.patronas.storage.datastore.transactions.TransactionsUseCase
import com.patronas.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
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

    private val _balances = MutableStateFlow(listOf<BalanceUiModel>())
    private val balances = _balances.asStateFlow()

    private val initialBalanceEUR = 1000.0

    init {
        viewModelScope.launch {
            withContext(dispatcher.background()) {
                fetchFakeRates()
                // fetchRates()
                setInitialTransactionCurrencies()
                setInitialBalances()
                observeBalances()
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
        makeTransaction = {
            makeTransaction(
                fromCurrency = selectedSellCurrency.value,
                sellAmount = sellAmount.value.toDouble(),
                toCurrency = selectedBuyCurrency.value,
                buyAmount = buyAmount.value.toDouble()
            )
        },
        sellAmount = sellAmount,
        buyAmount = buyAmount,
        updateSellAmount = {
            _sellAmount.value = it
            _buyAmount.value = calculateBuyAmount(amount = it)
        },
        balances = balances
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

        //update value for selected rate
        _buyAmount.value = calculateBuyAmount(amount = sellAmount.value)
    }

    private fun updateBuyCurrencies(currency: String) {
        _selectedBuyCurrency.value = currency

        //remove selected currency from sell list
        _sellCurrencies.value =
            ratesModel.value.currencies.toMutableList().filter { it != currency }

        //update value for selected rate
        _buyAmount.value = calculateBuyAmount(amount = sellAmount.value)
    }

    private suspend fun setInitialBalances() {
        transactionsUseCase.initBalance(
            initialAmount = initialBalanceEUR,
            primaryCurrency = EUR,
            currencies = ratesModel.value.currencies
        )
    }

    private fun makeTransaction(
        fromCurrency: String,
        sellAmount: Double,
        toCurrency: String,
        buyAmount: Double
    ) {
        viewModelScope.launch(dispatcher.background()) {
            transactionsUseCase.exchangeCurrency(
                fromCurrency = fromCurrency,
                sellAmount = sellAmount,
                toCurrency = toCurrency,
                buyAmount = buyAmount
            )
            val newBalance = transactionsUseCase.getBalance().first()
            Timber.tag("transaction")
                .i("$fromCurrency, newBalance: ${newBalance.currencies[fromCurrency]}")
            Timber.tag("transaction")
                .i("$toCurrency, newBalance: ${newBalance.currencies[toCurrency]}")
        }
    }

    private suspend fun observeBalances() {
        transactionsUseCase.getBalance().collect {
            _balances.value = it.toUiModel()
        }
    }

    private fun calculateBuyAmount(amount: String): String {
        val buyValue = amount.toDoubleOrNull()
        val sellCurrencyRate =
            ratesModel.value.getRateForCurrency(currency = selectedSellCurrency.value)
        val buyCurrencyRate =
            ratesModel.value.getRateForCurrency(currency = selectedBuyCurrency.value)

        //if selected buy currency is Base currency then divide, else multiple with rate
        buyValue?.let {
            return if (selectedBuyCurrency.value == ratesModel.value.baseRate) {
                buyCurrencyRate.div(sellCurrencyRate).times(buyValue).round()
            } else {
                buyCurrencyRate.times(sellCurrencyRate).times(buyValue).round()
            }
        }
        return ""
    }

    private fun fetchFakeRates() {
        _ratesModel.value = RatesDomainModel(
            baseRate = "EUR",
            currencies = listOf("EUR", "AFN", "AED", "USD", "TWD"),
            rates = listOf(
                RateModel(name = "EUR", rate = 1.0),
                RateModel(name = "AED", rate = 3.886633),
                RateModel(name = "AFN", rate = 94.698461),
                RateModel(name = "USD", rate = 1.058134),
                RateModel(name = "TWD", rate = 31.392738)
            )
        )
    }

}