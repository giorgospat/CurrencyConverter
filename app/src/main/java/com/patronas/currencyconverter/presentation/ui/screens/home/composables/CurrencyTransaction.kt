package com.patronas.currencyconverter.presentation.ui.screens.home.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import com.patronas.currencyconverter.presentation.extensions.isValidAmount
import com.patronas.currencyconverter.presentation.model.TransactionType
import com.patronas.currencyconverter.presentation.ui.screens.home.HomeUiState

@Composable
fun CurrencyTransaction(uiState: HomeUiState) {

    val sellCurrencies = uiState.sellCurrencies.collectAsState().value
    val buyCurrencies = uiState.buyCurrencies.collectAsState().value
    val selectedSellCurrency = uiState.selectedSellCurrency.collectAsState().value
    val selectedBuyCurrency = uiState.selectedBuyCurrency.collectAsState().value
    val sellAmount = uiState.sellAmount.collectAsState().value
    val buyAmount = uiState.buyAmount.collectAsState().value

    if (sellCurrencies.isNotEmpty() && buyCurrencies.isNotEmpty()) {

        Column {
            Text(text = stringResource(R.string.currency_exchange_label))
            //sell
            CurrencyExchangeRow(
                selectedCurrency = selectedSellCurrency,
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
                selectedCurrency = selectedBuyCurrency,
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
    updateAmount: (String) -> Unit,
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
            val prefix = "+".takeIf { type == TransactionType.BUY && amount.isNotEmpty() } ?: ""
            OutlinedTextField(
                value = prefix + amount,
                onValueChange = { amount ->
                    if (amount.isValidAmount()) {
                        updateAmount(amount)
                    }
                },
                modifier = Modifier.width(100.dp),
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

    if (expanded) {
        RateSelectionDialog(
            content = currencies,
            dismiss = {
                expanded = false
            },
            updateCurrency = {
                updateSelectedCurrency(it)
            }
        )
    }

}