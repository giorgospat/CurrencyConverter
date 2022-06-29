package com.patronas.currencyconverter.presentation.ui.screens.home.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.patronas.currencyconverter.R
import com.patronas.currencyconverter.presentation.model.TransactionType
import com.patronas.currencyconverter.presentation.ui.screens.home.HomeUiState

@Composable
fun CurrencyTransaction(uiState: HomeUiState) {

    val sellCurrencies = uiState.sellCurrencies.collectAsState().value
    val buyCurrencies = uiState.buyCurrencies.collectAsState().value
    val sellCurrency = uiState.selectedSellCurrency.collectAsState().value
    val buyCurrency = uiState.selectedBuyCurrency.collectAsState().value
    val sellAmount = uiState.sellAmount.collectAsState().value
    val buyAmount = uiState.buyAmount.collectAsState().value

    if (sellCurrencies.isNotEmpty() && buyCurrencies.isNotEmpty()) {

        Column {
            Text(text = stringResource(R.string.currency_exchange_label))
            //sell
            CurrencyExchangeRow(
                selectedCurrency = sellCurrency,
                currencies = sellCurrencies,
                amount = sellAmount,
                updateAmount = {
                    uiState.updateSellAmount(it)
                },
                label = stringResource(R.string.sell_label),
                icon = R.drawable.ic_up_arrow,
                updateSelectedCurrency = {
                    uiState.updateSellCurrency(it)
                },
                type = TransactionType.SELL
            )
            //buy
            CurrencyExchangeRow(
                selectedCurrency = buyCurrency,
                currencies = buyCurrencies,
                amount = buyAmount,
                updateAmount = {
                },
                label = stringResource(R.string.receive_label),
                icon = R.drawable.ic_down_arrow,
                updateSelectedCurrency = {
                    uiState.updateBuyCurrency(it)
                },
                type = TransactionType.BUY
            )
        }
    }
}

@Composable
fun CurrencyExchangeRow(
    selectedCurrency: String,
    currencies: List<String>,
    amount: String,
    updateAmount: (Double) -> Unit,
    label: String,
    @DrawableRes icon: Int,
    updateSelectedCurrency: (String) -> Unit,
    type: TransactionType
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "CurrencyExchangeIcon",
                modifier = Modifier.size(50.dp)
            )
            Text(text = label, modifier = Modifier.padding(start = 10.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            val prefix = "+".takeIf { type == TransactionType.BUY } ?: ""
            BasicTextField(
                value = prefix + amount,
                onValueChange = { amount ->
                    amount.toDoubleOrNull()?.let {
                        updateAmount(it)
                    }
                },
                modifier = Modifier.width(50.dp),
                readOnly = type == TransactionType.BUY,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            )
            Box(modifier = Modifier.padding(start = 20.dp)) {
                DropDownList(
                    selectedCurrency = selectedCurrency,
                    currencies = currencies,
                    updateSelectedCurrency = {
                        updateSelectedCurrency(it)
                    }
                )
            }
        }
    }
    Divider()
}

@Composable
fun DropDownList(
    selectedCurrency: String,
    currencies: List<String>,
    updateSelectedCurrency: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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
        modifier = Modifier.height(300.dp)
    ) {
        currencies.forEach { label ->
            DropdownMenuItem(onClick = {
                expanded = false
                updateSelectedCurrency(label)
            }) {
                Text(text = label)
            }
        }
    }
}