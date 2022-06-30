package com.patronas.storage.datastore.transactions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.patronas.storage.datastore.PreferencesKeys.TRANSACTION_DATES
import com.patronas.storage.datastore.PreferencesKeys.USER_BALANCES
import com.patronas.storage.model.UserBalanceModel
import com.patronas.storage.model.transaction.TransactionResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.reflect.Type
import java.util.*


class TransactionsUseCaseImpl(private val context: Context, moshi: Moshi) : TransactionsUseCase {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "transactionPrefs")
    private val commaSeparator = ","

    var balancesMoshiType: Type = Types.newParameterizedType(
        MutableMap::class.java,
        String::class.java,
        Double::class.javaObjectType
    )
    var balancesAdapter: JsonAdapter<Map<String, Double>> = moshi.adapter(balancesMoshiType)


    override suspend fun initBalance(
        initialAmount: Double,
        primaryCurrency: String,
        currencies: List<String>
    ) {
        context.dataStore.edit { prefs ->
            /**
            Already initialized with initial amount
             */
            prefs[USER_BALANCES]?.let {
                val savedBalances: MutableMap<String, Double> =
                    balancesAdapter.fromJson(it) as MutableMap<String, Double>

                //insert only NEW currencies from API - previous currencies with balances will be unchanged
                savedBalances.putAll(
                    currencies
                        //keep base currency first in the list
                        .sortedWith(compareBy<String> { it != primaryCurrency }.thenBy { it })
                        .associateWith { 0.0 }
                        .filter { !savedBalances.contains(it.key) }
                )
                //save updated map
                prefs[USER_BALANCES] = balancesAdapter.toJson(savedBalances)

            } ?: run {
                /**
                First time initializing
                 */
                val balancesMap = currencies
                    //keep base currency first in the list
                    .sortedWith(compareBy<String> { it != primaryCurrency }.thenBy { it })
                    .associateWith { 0.0 }.toMutableMap()
                balancesMap[primaryCurrency] = initialAmount
                prefs[USER_BALANCES] = balancesAdapter.toJson(balancesMap)
            }
        }
    }

    override fun getBalance(): Flow<UserBalanceModel> {
        return context.dataStore.data
            .map { prefs ->
                prefs[USER_BALANCES]?.let {
                    UserBalanceModel(currencies = balancesAdapter.fromJson(it) as MutableMap<String, Double>)
                } ?: UserBalanceModel()
            }.also {
                it.map { it.currencies.toSortedMap() }
            }
    }

    override suspend fun exchangeCurrency(
        fromCurrency: String,
        sellAmount: Double,
        toCurrency: String,
        buyAmount: Double,
        date: Date
    ): TransactionResponse {
        return try {
            context.dataStore.edit { prefs ->
                prefs[USER_BALANCES]?.let {
                    //fetch map
                    val savedBalances: MutableMap<String, Double> =
                        balancesAdapter.fromJson(it) as MutableMap<String, Double>

                    //make transactions
                    savedBalances[fromCurrency]?.let { currentAmount ->
                        savedBalances[fromCurrency] = currentAmount - sellAmount
                    }
                    savedBalances[toCurrency]?.let { currentAmount ->
                        savedBalances[toCurrency] = currentAmount + buyAmount
                    }

                    //save updated map
                    prefs[USER_BALANCES] = balancesAdapter.toJson(savedBalances)
                    storeTransactionDate(date = date, prefs = prefs)
                }
            }
            TransactionResponse.Success
        } catch (e: Exception) {
            TransactionResponse.Error
        }
    }

    override fun getTransactionHistory(): Flow<List<Date>> {
        return context.dataStore.data
            .map { prefs ->
                prefs[TRANSACTION_DATES]?.split(commaSeparator)?.map {
                    Date(it.toLong())
                } ?: listOf()
            }
    }

    private fun storeTransactionDate(date: Date, prefs: MutablePreferences) {
        val savedTransactions = prefs[TRANSACTION_DATES]?.split(commaSeparator)?.toMutableList()

        savedTransactions?.let {
            it.add(date.time.toString())
            prefs[TRANSACTION_DATES] = it
                .filter { it.isNotEmpty() }
                .joinToString(separator = commaSeparator)

        } ?: run {//first time
            prefs[TRANSACTION_DATES] = date.time.toString()
        }
    }

}