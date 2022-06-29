package com.patronas.currencyconverter.presentation.ui.screens.home.composables

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.patronas.currencyconverter.R

@Composable
fun InfoDialog(onDismiss: () -> Unit, title: String, message: String) {

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text(stringResource(R.string.popup_ok_button))
            }
        }
    )

}