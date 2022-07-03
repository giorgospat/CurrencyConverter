package com.patronas.currencyconverter.presentation.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patronas.currencyconverter.R
import com.patronas.currencyconverter.presentation.ui.screens.home.HomeUiEvent.*
import com.patronas.currencyconverter.presentation.ui.screens.home.composables.*

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {

    val uiState = viewModel.uiState
    val uiEvent = viewModel.uiEvent.collectAsState().value
    var dataError by remember { mutableStateOf(false) }
    val balances = uiState.balances.collectAsState().value

    /**
     * subscribe to UI events
     */
    when (uiEvent) {
        is ExchangeCompleted -> {
            InfoDialog(
                onDismiss = { uiState.dismissDialog() },
                title = stringResource(id = R.string.dialog_currency_success_convertion_title),
                message = uiEvent.message
            )
        }
        is InputAmountIncorrectError -> {
            InfoDialog(
                onDismiss = { uiState.dismissDialog() },
                title = stringResource(id = R.string.currency_conversion_error_title),
                message = stringResource(id = R.string.incorrect_amount_error)
            )
        }
        is InsufficientBalanceError -> {
            InfoDialog(
                onDismiss = { uiState.dismissDialog() },
                title = stringResource(id = R.string.currency_conversion_error_title),
                message = stringResource(id = R.string.insufficient_balance_error)
            )
        }
        is TransactionError -> {
            InfoDialog(
                onDismiss = { uiState.dismissDialog() },
                title = stringResource(id = R.string.currency_conversion_error_title),
                message = stringResource(id = R.string.dialog_generic_transaction_error_message)
            )
        }
        is FeesExplanation -> {
            InfoDialog(
                onDismiss = { uiState.dismissDialog() },
                title = stringResource(id = R.string.fees_explanation_dialog_title),
                message = uiEvent.message
            )
        }
        is LoadingRatesError -> {
            InfoDialog(
                onDismiss = { uiState.dismissDialog() },
                title = stringResource(id = R.string.dialog_loading_rates_error_title),
                message = stringResource(id = R.string.dialog_loading_rates_error_message)
            )
            dataError = true
        }
        is Default -> {}
    }


    /**
     * Main layout
     */

    if (balances.isEmpty()) {
        Loader()
    } else if (!dataError) {
        LazyColumn(modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp)) {
            item {
                RatesHorizontalList(
                    balances = balances,
                    onBalanceClick = {
                        uiState.updateSellCurrency(it)
                    }
                )
            }
            item {
                CurrencyTransaction(uiState = uiState)
            }
            item {
                SubmitButton(onSubmit = { uiState.makeTransaction() })
            }
            item {
                FeeText(
                    fee = uiState.transactionFee.collectAsState().value,
                    currency = uiState.selectedSellCurrency.collectAsState().value,
                    onLearnMore = {
                        uiState.onFeeLearnMore()
                    }
                )
            }
        }
    }

}