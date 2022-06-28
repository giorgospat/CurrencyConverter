package com.patronas.currencyconverter.presentation.ui.screens.home.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import com.patronas.currencyconverter.presentation.ui.screens.home.HomeUiState

@Composable
fun CurrencyTransaction(uiState: HomeUiState) {
    if (uiState.ratesModel.collectAsState().value.currencies.isNotEmpty()) {
        Column {
            SellCurrencies(currencies = uiState.ratesModel.collectAsState().value.currencies)
            BuyCurrencies(currencies = uiState.ratesModel.collectAsState().value.currencies)
        }
    }
}

@Composable
fun SellCurrencies(currencies: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCurrency by remember { mutableStateOf("") }

    Button(onClick = { expanded = !expanded }) {
        Text(selectedCurrency)
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = null,
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
    ) {
        currencies.forEach { label ->
            DropdownMenuItem(onClick = {
                expanded = false
                selectedCurrency = label
            }) {
                Text(text = label)
            }
        }
    }
}

@Composable
fun BuyCurrencies(currencies: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCurrency by remember { mutableStateOf("") }

    Button(onClick = { expanded = !expanded }) {
        Text(selectedCurrency)
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = null,
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
    ) {
        currencies.forEach { label ->
            DropdownMenuItem(onClick = {
                expanded = false
                selectedCurrency = label
            }) {
                Text(text = label)
            }
        }
    }
}