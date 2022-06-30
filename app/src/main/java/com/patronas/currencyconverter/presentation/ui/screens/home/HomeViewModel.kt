package com.patronas.currencyconverter.presentation.ui.screens.home

import androidx.lifecycle.viewModelScope
import com.patronas.currencyconverter.R
import com.patronas.currencyconverter.base.BaseViewModel
import com.patronas.currencyconverter.presentation.extensions.*
import com.patronas.currencyconverter.presentation.model.BalanceUiModel
import com.patronas.currencyconverter.presentation.model.toUiModel
import com.patronas.data.base.DomainApiResult
import com.patronas.domain.EUR
import com.patronas.domain.USD
import com.patronas.domain.config.TransactionFeeConfiguration.baseFeePercent
import com.patronas.domain.config.TransactionFeeConfiguration.dailyLimitForBaseFee
import com.patronas.domain.config.TransactionFeeConfiguration.extraFeeAmount
import com.patronas.domain.config.TransactionFeeConfiguration.extraFeePercent
import com.patronas.domain.config.TransactionFeeConfiguration.freeExchanges
import com.patronas.domain.config.TransactionFeeConfiguration.zeroFee
import com.patronas.domain.extensions.getRateForCurrency
import com.patronas.domain.model.RatesDomainModel
import com.patronas.domain.model.reusable.RateModel
import com.patronas.domain.usecase.rates.GetRatesUseCase
import com.patronas.domain.usecase.rates.transaction_fee.TransactionFeeUseCase
import com.patronas.storage.datastore.transactions.TransactionsUseCase
import com.patronas.storage.model.transaction.TransactionError
import com.patronas.storage.model.transaction.TransactionResponse
import com.patronas.utils.DateProvider
import com.patronas.utils.DispatcherProvider
import com.patronas.utils.ResourcesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val ratesUseCase: GetRatesUseCase,
    private val transactionsUseCase: TransactionsUseCase,
    private val date: DateProvider,
    private val transactionFeeUseCase: TransactionFeeUseCase,
    private val resourcesRepo: ResourcesRepo
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

    private val _transactionFee = MutableStateFlow(zeroFee)
    private val transactionFee = _transactionFee.asStateFlow()

    private val _balances = MutableStateFlow(listOf<BalanceUiModel>())
    private val balances = _balances.asStateFlow()

    private val _uiEvent = MutableStateFlow<HomeUiEvent>(HomeUiEvent.Default)
    val uiEvent = _uiEvent.asStateFlow()

    private val initialBalanceEUR = 1000.0

    init {
        viewModelScope.launch {
            withContext(dispatcher.background()) {
                fetchFakeRates()
                //fetchRates()
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
            updateSellCurrenciesList(currency = it)
        },
        updateBuyCurrency = {
            updateBuyCurrenciesList(currency = it)
        },
        makeTransaction = {
            makeTransaction(
                fromCurrency = selectedSellCurrency.value,
                sellAmount = sellAmount.value,
                toCurrency = selectedBuyCurrency.value,
                buyAmount = buyAmount.value
            )
        },
        sellAmount = sellAmount,
        buyAmount = buyAmount,
        updateSellAmount = {
            updateSellAmount(amount = it)
        },
        balances = balances,
        transactionFee = transactionFee,
        dismissDialog = {
            dismissDialog()
        },
        onFeeLearnMore = {
            showFeeExplanation()
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
        _sellCurrencies.value = ratesModel.value.currencies
        _buyCurrencies.value = ratesModel.value.currencies

        _selectedSellCurrency.value = ratesModel.value.currencies.find { it == EUR } ?: ""
        _selectedBuyCurrency.value = ratesModel.value.currencies.find { it == USD } ?: ""
    }

    private fun updateSellCurrenciesList(currency: String) {

        //reverse currencies if selected sell currency is same as buy currency
        if (selectedBuyCurrency.value == currency) {
            _selectedBuyCurrency.value = selectedSellCurrency.value
        }
        _selectedSellCurrency.value = currency

        //update value for selected rate
        _buyAmount.value = calculateBuyAmount(amount = sellAmount.value)

        calculateFee()
    }

    private fun updateBuyCurrenciesList(currency: String) {

        //reverse currencies if selected buy currency is same as sell currency
        if (selectedSellCurrency.value == currency) {
            _selectedSellCurrency.value = selectedBuyCurrency.value
        }
        _selectedBuyCurrency.value = currency

        //update value for selected rate
        _buyAmount.value = calculateBuyAmount(amount = sellAmount.value)

        calculateFee()
    }

    private fun updateSellAmount(amount: String) {
        _sellAmount.value = amount
        _buyAmount.value = calculateBuyAmount(amount = amount)
        calculateFee()
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
        sellAmount: String,
        toCurrency: String,
        buyAmount: String
    ) {

        if (!sellAmount.isValidDouble() || !buyAmount.isValidDouble()) {
            _uiEvent.tryEmit(HomeUiEvent.InputAmountIncorrectError)
            return
        }

        viewModelScope.launch(dispatcher.background()) {

            calculateFee()

            if (sellAmount.hasValidBalance(
                    balance = transactionsUseCase.getBalance().first()
                        .getBalanceFor(currency = fromCurrency),
                    fee = transactionFee.value
                )
            ) {

                when (val transactionResult = transactionsUseCase.exchangeCurrency(
                    fromCurrency = fromCurrency,
                    sellAmount = sellAmount.toDouble() + transactionFee.value,
                    toCurrency = toCurrency,
                    buyAmount = buyAmount.toDouble(),
                    date = date.getCurrentDate()
                )) {
                    is TransactionResponse.Success -> {
                        var message = resourcesRepo.getString(
                            R.string.success_exchange_message_with_fee,
                            sellAmount,
                            fromCurrency,
                            buyAmount,
                            toCurrency
                        )
                        if (transactionFee.value > 0) {
                            message += resourcesRepo.getString(
                                R.string.success_exchange_message_commission_fee,
                                transactionFee.value.round(),
                                ratesModel.value.baseRate
                            )
                        }

                        _uiEvent.emit(HomeUiEvent.ExchangeCompleted(message = message))
                    }

                    is TransactionResponse.Error -> {
                        when (transactionResult.error) {
                            TransactionError.INSUFFICIENT_BALANCE -> {
                                _uiEvent.tryEmit(HomeUiEvent.InsufficientBalanceError)
                            }
                            TransactionError.INVALID_AMOUNT -> {
                                _uiEvent.tryEmit(HomeUiEvent.InputAmountIncorrectError)
                            }
                        }
                    }
                }
            } else {
                _uiEvent.tryEmit(HomeUiEvent.InsufficientBalanceError)
            }
        }
    }

    private suspend fun observeBalances() {
        transactionsUseCase.getBalance().collect {
            _balances.value = it.toUiModel(firstCurrency = ratesModel.value.baseRate)
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
            return buyValue.convertToBaseCurrency(rate = buyCurrencyRate).div(sellCurrencyRate)
                .round()
        } ?: run {
            return ""
        }
    }

    private fun calculateFee() {
        viewModelScope.launch(dispatcher.background()) {
            _transactionFee.value = transactionFeeUseCase.calculateFee(
                baseCurrencyRate = ratesModel.value.getRateForCurrency(currency = ratesModel.value.baseRate),
                currencyRate = ratesModel.value.getRateForCurrency(currency = selectedSellCurrency.value),
                amount = sellAmount.value.toDoubleOrDefault(),
                currentDate = date.getCurrentDate(),
                transactionHistory = transactionsUseCase.getTransactionHistory().first()
            )
        }
    }

    private fun dismissDialog() {
        _uiEvent.tryEmit(HomeUiEvent.Default)
    }

    private fun showFeeExplanation() {
        val explanationMessage = resourcesRepo.getString(
            R.string.fees_explanation_message,
            freeExchanges,
            (baseFeePercent * 100).round(),
            dailyLimitForBaseFee,
            (extraFeePercent * 100).round(),
            extraFeeAmount,
            ratesModel.value.baseRate
        )

        _uiEvent.tryEmit(HomeUiEvent.FeesExplanation(message = explanationMessage))
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