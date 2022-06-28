package com.patronas.currencyconverter.presentation.ui.screens.home

import androidx.lifecycle.viewModelScope
import com.patronas.currencyconverter.base.BaseViewModel
import com.patronas.data.base.DomainApiResult
import com.patronas.domain.model.RatesDomainModel
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

    private val _ratesModel = MutableStateFlow(RatesDomainModel())
    private val ratesModel = _ratesModel.asStateFlow()

    init {
        fetchRates()
    }

    val uiState = HomeUiState(
        ratesModel = ratesModel
    )

    private fun fetchRates() {
        viewModelScope.launch(dispatcher.background()) {
            when (val request = ratesUseCase.getRates()) {
                is DomainApiResult.Success -> {
                    _ratesModel.value = request.data
                }
                is DomainApiResult.Error -> {
                    //TODO handle error
                }
            }
        }
    }
}