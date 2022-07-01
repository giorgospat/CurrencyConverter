package com.patronas.currencyconverter.presentation.ui.screens.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RateSelectionDialog(
    content: List<String>,
    dismiss: () -> Unit,
    updateCurrency: (String) -> Unit
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {
            dismiss()
        }
    ) {

        Card(
            shape = RoundedCornerShape(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .width(250.dp)
                    .height(400.dp)
                    .background(Color.White)
            ) {
                items(content) {
                    CurrencyText(
                        text = it,
                        dismiss = { dismiss() },
                        updateCurrency = { updateCurrency(it) })
                }
            }
        }
    }
}

@Composable
fun CurrencyText(
    text: String,
    dismiss: () -> Unit,
    updateCurrency: (String) -> Unit
) {
    Column(
        modifier = Modifier.clickable {
            updateCurrency(text)
            dismiss()
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            modifier = Modifier.padding(15.dp)
        )
        Divider()
    }
}