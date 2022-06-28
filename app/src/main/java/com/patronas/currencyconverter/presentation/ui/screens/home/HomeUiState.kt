package com.patronas.currencyconverter.presentation.ui.screens.home

import com.patronas.domain.model.RatesDomainModel
import kotlinx.coroutines.flow.StateFlow

data class HomeUiState(
    val ratesModel: StateFlow<RatesDomainModel>
)