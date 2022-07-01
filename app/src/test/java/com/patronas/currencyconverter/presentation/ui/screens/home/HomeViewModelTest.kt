package com.patronas.currencyconverter.presentation.ui.screens.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.patronas.currencyconverter.presentation.model.toUiModel
import com.patronas.data.base.DomainApiResult
import com.patronas.domain.model.RatesDomainModel
import com.patronas.domain.model.reusable.RateModel
import com.patronas.domain.usecase.rates.GetRatesUseCase
import com.patronas.domain.usecase.rates.transaction_fee.TransactionFeeUseCase
import com.patronas.domain.usecase.rates.transaction_fee.TransactionFeeUseCaseImpl
import com.patronas.storage.datastore.transactions.TransactionsUseCase
import com.patronas.storage.model.UserBalanceModel
import com.patronas.storage.model.transaction.TransactionResponse
import com.patronas.utils.DateProvider
import com.patronas.utils.DispatcherProviderImpl
import com.patronas.utils.ResourcesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import java.util.*


@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var ratesUseCase: GetRatesUseCase

    @Mock
    lateinit var transactionsUseCase: TransactionsUseCase

    @Mock
    lateinit var resourcesRepo: ResourcesRepo

    @Mock
    lateinit var dateProvider: DateProvider

    private lateinit var viewModel: HomeViewModel
    private val transactionFeeUseCase: TransactionFeeUseCase = TransactionFeeUseCaseImpl()
    private val dispatcher = DispatcherProviderImpl(
        mainThread = Dispatchers.Unconfined,
        backgroundThread = Dispatchers.Unconfined,
        default = Dispatchers.Unconfined
    )
    private lateinit var uiState: HomeUiState

    private val ratesSuccessResponse = DomainApiResult.Success(
        fakeRates()
    )

    private val dummyBalances = flow {
        emit(
            UserBalanceModel(
                currencies = mutableMapOf(
                    Pair("AED", 0.0),
                    Pair("AFN", 0.0),
                    Pair("USD", 0.0),
                    Pair("TWD", 0.0),
                    Pair("EUR", 1000.0)
                )
            )
        )
    }

    private val dummyTransactionDates = flow {
        emit(listOf(dummyDate))
    }

    private val dummyDate = Calendar.getInstance().time

    @Before
    fun init() {
        MockitoAnnotations.openMocks(this)
        initViewModel()
        uiState = viewModel.uiState
        mockDependencies()
    }

    @Test
    fun `when screen is loaded verify rates are displayed`() {
        assertEquals(uiState.ratesModel.value, fakeRates())
    }

    @Test
    fun `when screen is loaded verify currencies for transaction are initialized`() {
        //all currencies in dropdown list
        assertEquals(uiState.sellCurrencies.value, listOf("AFN", "AED", "USD", "TWD", "EUR"))
        assertEquals(uiState.buyCurrencies.value, listOf("AFN", "AED", "USD", "TWD", "EUR"))

        //selected currencies in dropdown list
        assertEquals(uiState.selectedSellCurrency.value, "EUR")
        assertEquals(uiState.selectedBuyCurrency.value, "USD")
    }

    @Test
    fun `when screen is loaded verify user balance is emitted`() {
        runTest {
            Mockito.`when`(
                transactionsUseCase.getBalance()
            ).thenReturn(
                dummyBalances
            )

            viewModel.observeUserBalances()

            assertEquals(
                uiState.balances.value, dummyBalances.first().toUiModel()
            )
        }
    }

    @Test
    fun `when sell currency is updated verify selected sell currency is updated`() {
        uiState.updateSellCurrency("AED")
        assertEquals(uiState.selectedSellCurrency.value, "AED")
    }

    @Test
    fun `when buy currency is updated verify selected buy currency is updated`() {
        uiState.updateBuyCurrency("TWD")
        assertEquals(uiState.selectedBuyCurrency.value, "TWD")
    }

    @Test
    fun `when selected sell currency is same as buy currency verify currencies are reversed`() {
        //first we have 2 different currencies selected
        uiState.updateSellCurrency("EUR")
        uiState.updateBuyCurrency("USD")

        //then user selects the same currency as buy currency
        uiState.updateSellCurrency("USD")

        //currencies should be reversed
        assertEquals(uiState.selectedSellCurrency.value, "USD")
        assertEquals(uiState.selectedBuyCurrency.value, "EUR")
    }

    @Test
    fun `when user makes a transaction that is success verify success message is emitted`() {
        runTest {

            uiState.updateSellCurrency("EUR")
            uiState.updateSellAmount("10.0")

            Mockito.`when`(
                transactionsUseCase.exchangeCurrency(
                    fromCurrency = uiState.selectedSellCurrency.value,
                    sellAmount = uiState.sellAmount.value.toDouble() + uiState.transactionFee.value,
                    toCurrency = uiState.selectedBuyCurrency.value,
                    buyAmount = uiState.buyAmount.value.toDouble(),
                    date = dateProvider.getCurrentDate()
                )
            ).thenReturn(
                TransactionResponse.Success
            )

            uiState.makeTransaction()

            assertEquals(
                viewModel.uiEvent.value,
                HomeUiEvent.ExchangeCompleted(message = "success")
            )
        }
    }

    @Test
    fun `when user makes a transaction with error verify error message is emitted`() {
        runTest {

            uiState.updateSellCurrency("EUR")
            uiState.updateSellAmount("10.0")

            Mockito.`when`(
                transactionsUseCase.exchangeCurrency(
                    fromCurrency = uiState.selectedSellCurrency.value,
                    sellAmount = uiState.sellAmount.value.toDouble() + uiState.transactionFee.value,
                    toCurrency = uiState.selectedBuyCurrency.value,
                    buyAmount = uiState.buyAmount.value.toDouble(),
                    date = dateProvider.getCurrentDate()
                )
            ).thenReturn(
                TransactionResponse.Error
            )

            uiState.makeTransaction()

            assert(viewModel.uiEvent.value is HomeUiEvent.TransactionError)
        }
    }

    @Test
    fun `when user makes a transaction with Insufficient Balance verify balance error message is emitted`() {
        runTest {

            uiState.updateSellCurrency("EUR")
            uiState.updateSellAmount("1200.0") //200 euro more than his current balance

            Mockito.`when`(
                transactionsUseCase.exchangeCurrency(
                    fromCurrency = uiState.selectedSellCurrency.value,
                    sellAmount = uiState.sellAmount.value.toDouble() + uiState.transactionFee.value,
                    toCurrency = uiState.selectedBuyCurrency.value,
                    buyAmount = uiState.buyAmount.value.toDouble(),
                    date = dateProvider.getCurrentDate()
                )
            ).thenReturn(
                TransactionResponse.Error
            )

            uiState.makeTransaction()

            assert(viewModel.uiEvent.value is HomeUiEvent.InsufficientBalanceError)
        }
    }

    @Test
    fun `when sell or input amount is not valid verify input error message is emitted`() {
        runTest {

            uiState.updateSellAmount("10,20") //wrong input format

            uiState.makeTransaction()

            assert(viewModel.uiEvent.value is HomeUiEvent.InputAmountIncorrectError)

            uiState.updateBuyCurrency("test") //wrong input format

            uiState.makeTransaction()

            assert(viewModel.uiEvent.value is HomeUiEvent.InputAmountIncorrectError)
        }
    }


    /**
     * Helper functions
     */

    private fun initViewModel() {
        viewModel = HomeViewModel(
            dispatcher = dispatcher,
            ratesUseCase = ratesUseCase,
            transactionsUseCase = transactionsUseCase,
            date = dateProvider,
            transactionFeeUseCase = transactionFeeUseCase,
            resourcesRepo = resourcesRepo
        )
    }

    private fun fakeRates(): RatesDomainModel {
        return RatesDomainModel(
            baseRate = "EUR",
            currencies = listOf("AFN", "AED", "USD", "TWD", "EUR"),
            rates = listOf(
                RateModel(name = "AED", rate = 3.886633),
                RateModel(name = "AFN", rate = 94.698461),
                RateModel(name = "USD", rate = 1.058134),
                RateModel(name = "TWD", rate = 31.392738),
                RateModel(name = "EUR", rate = 1.0)
            )
        )
    }

    private fun mockDependencies() {
        runTest {
            Mockito.`when`(
                ratesUseCase.getRates()
            ).thenReturn(
                ratesSuccessResponse
            )

            Mockito.`when`(
                dateProvider.getCurrentDate()
            ).thenReturn(
                dummyDate
            )

            Mockito.`when`(
                resourcesRepo.getString(any(), any())
            ).thenReturn(
                "success"
            )

            //set storage properties
            Mockito.`when`(
                transactionsUseCase.getBalance()
            ).thenReturn(
                dummyBalances
            )

            Mockito.`when`(
                transactionsUseCase.getTransactionHistory()
            ).thenReturn(
                dummyTransactionDates
            )
        }
    }

}