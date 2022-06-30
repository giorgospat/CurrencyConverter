package com.patronas.currencyconverter.presentation.ui.screens.home.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patronas.currencyconverter.R
import com.patronas.currencyconverter.presentation.extensions.round

@Composable
fun FeeText(fee: Double, onLearnMore: () -> Unit) {
    if (fee > 0) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.current_fee_label, fee.round()),
                fontSize = 12.sp
            )
            Text(
                text = stringResource(id = R.string.fees_learn_more),
                textDecoration = TextDecoration.Underline,
                fontSize = 12.sp,
                modifier = Modifier.clickable {
                    onLearnMore()
                }
            )
        }
    }
}