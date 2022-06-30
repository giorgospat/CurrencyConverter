package com.patronas.currencyconverter.presentation.ui.screens.home.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patronas.currencyconverter.R
import com.patronas.currencyconverter.presentation.extensions.round

@Composable
fun FeeText(fee: Double) {
    if (fee > 0) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
        ) {
            Text(
                text = stringResource(id = R.string.current_fee_label, fee.round()),
                fontSize = 12.sp
            )
        }
    }
}