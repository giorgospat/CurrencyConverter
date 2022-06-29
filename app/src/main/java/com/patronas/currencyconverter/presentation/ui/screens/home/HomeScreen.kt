package com.patronas.currencyconverter.presentation.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patronas.currencyconverter.presentation.ui.screens.home.composables.CurrencyTransaction
import com.patronas.currencyconverter.presentation.ui.screens.home.composables.RatesHorizontalList
import com.patronas.currencyconverter.presentation.ui.screens.home.composables.SubmitButton

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {

    val uiState = viewModel.uiState

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