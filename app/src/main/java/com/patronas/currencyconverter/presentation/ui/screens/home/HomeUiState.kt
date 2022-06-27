package com.patronas.currencyconverter.presentation.ui.screens.home

import com.patronas.domain.model.reusable.RateModel
import kotlinx.coroutines.flow.StateFlow

data class HomeUiState(
    val rates: StateFlow<List<RateModel>>,
    val baseRate: StateFlow<String>
)