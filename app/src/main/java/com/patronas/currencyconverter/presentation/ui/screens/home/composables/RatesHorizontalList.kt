package com.patronas.currencyconverter.presentation.ui.screens.home.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patronas.currencyconverter.R
import com.patronas.currencyconverter.presentation.model.BalanceUiModel

@Composable
fun RatesHorizontalList(balances: List<BalanceUiModel>) {

    Column {
        Text(text = stringResource(R.string.my_balances_label))
        LazyRow(modifier = Modifier.padding(vertical = 30.dp)) {
            items(balances) {
                BalanceItem(item = it)
            }
        }
    }
}

@Composable
fun BalanceItem(item: BalanceUiModel) {
    Row(modifier = Modifier.padding(end = 30.dp)) {
        Text(text = item.amount, fontWeight = FontWeight.Bold)
        Text(
            text = item.currency,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 3.dp)
        )
    }
}

