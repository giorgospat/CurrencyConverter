package com.patronas.currencyconverter.presentation.ui.screens.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patronas.currencyconverter.presentation.ui.screens.home.composables.RatesHorizontalList

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {

    val uiState = viewModel.uiState

    LazyColumn {
        item {
            RatesHorizontalList()
        }
    }

}