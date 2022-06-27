package com.patronas.currencyconverter.presentation.ui.screens.home

import androidx.lifecycle.viewModelScope
import com.patronas.currencyconverter.base.BaseViewModel
import com.patronas.data.base.DomainApiResult
import com.patronas.domain.model.reusable.RateModel
import com.patronas.domain.usecase.GetRatesUseCase
import com.patronas.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val ratesUseCase: GetRatesUseCase
) : BaseViewModel() {

    private val _baseRate = MutableStateFlow("")
    private val baseRate = _baseRate.asStateFlow()

    private val _rates = MutableStateFlow(listOf<RateModel>())
    private val rates = _rates.asStateFlow()


    init {
        fetchRates()
    }

    val uiState = HomeUiState(
        baseRate = baseRate,
        rates = rates
    )

    private fun fetchRates() {
        viewModelScope.launch(dispatcher.background()) {
            when (val request = ratesUseCase.getRates()) {
                is DomainApiResult.Success -> {
                    _baseRate.value = request.data.baseRate
                    _rates.value = request.data.rates
                }
                is DomainApiResult.Error -> {
                    //TODO handle error
                }
            }
        }
    }
}