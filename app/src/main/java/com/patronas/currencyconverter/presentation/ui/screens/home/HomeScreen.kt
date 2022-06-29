package com.patronas.currencyconverter.presentation.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patronas.currencyconverter.R
import com.patronas.currencyconverter.presentation.ui.screens.home.HomeUiEvent.*
import com.patronas.currencyconverter.presentation.ui.screens.home.composables.CurrencyTransaction
import com.patronas.currencyconverter.presentation.ui.screens.home.composables.InfoDialog
import com.patronas.currencyconverter.presentation.ui.screens.home.composables.RatesHorizontalList
import com.patronas.currencyconverter.presentation.ui.screens.home.composables.SubmitButton

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {

    val uiState = viewModel.uiState
    val uiEvent = viewModel.uiEvent.collectAsState().value

    /**
     * handle events
     */
    when (uiEvent) {
        is ExchangeCompleted -> {
            InfoDialog(
                onDismiss = {
                    uiState.dismissDialog()
                },
                title = stringResource(id = R.string.popup_currency_converted_title),
                message = uiEvent.message
            )
        }
        is InputAmountEmptyError -> {
            InfoDialog(
                onDismiss = {
                    uiState.dismissDialog()
                },
                title = stringResource(id = R.string.currency_conversion_error_title),
                message = stringResource(id = R.string.empty_amount_error)
            )
        }
        is InsufficientBalanceError -> {
            InfoDialog(
                onDismiss = {
                    uiState.dismissDialog()
                },
                title = stringResource(id = R.string.currency_conversion_error_title),
                message = stringResource(id = R.string.insufficient_balance_error)
            )
        }
        is Default -> {}
    }

    /**
     * create UI
     */
    LazyColumn(modifier = Modifier.padding(10.dp)) {
        item {
            RatesHorizontalList(balances = uiState.balances.collectAsState().value)
        }
        item {
            CurrencyTransaction(uiState = uiState)
        }
        item {
            SubmitButton(onSubmit = { uiState.makeTransaction() })
        }
    }

}