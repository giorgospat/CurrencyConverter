package com.patronas.currencyconverter.presentation.ui.screens.home.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.patronas.currencyconverter.R

@Composable
fun SubmitButton(onSubmit: () -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            modifier = Modifier.padding(top = 30.dp),
            onClick = { onSubmit() },
        ) {
            Text(
                text = stringResource(R.string.submit_btn_label),
                modifier = Modifier.width(100.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}