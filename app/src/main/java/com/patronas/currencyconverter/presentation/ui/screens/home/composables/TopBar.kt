package com.patronas.currencyconverter.presentation.ui.screens.home.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patronas.currencyconverter.R

@Composable
fun TopBar() {
    Text(
        text = stringResource(id = R.string.app_name),
        color = Color.DarkGray,
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
}