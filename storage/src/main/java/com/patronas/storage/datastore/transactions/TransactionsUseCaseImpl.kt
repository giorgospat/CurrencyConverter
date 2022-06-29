package com.patronas.storage.datastore.transactions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.patronas.storage.extensions.isValidDouble
import com.patronas.storage.model.UserBalanceModel
import com.patronas.storage.model.transaction.TransactionError
import com.patronas.storage.model.transaction.TransactionResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.reflect.Type


class TransactionsUseCaseImpl(private val context: Context, moshi: Moshi) : TransactionsUseCase {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "transactionPrefs")
    private val USER_BALANCES = stringPreferencesKey("user_balances")

    var moshiType: Type = Types.newParameterizedType(
        MutableMap::class.java,
        String::class.java,
        Double::class.javaObjectType
    )
    var adapter: JsonAdapter<Map<String, Double>> = moshi.adapter(moshiType)

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
                    adapter.fromJson(it) as MutableMap<String, Double>

                //insert only NEW currencies from API - previous currencies with balances will be unchanged
                savedBalances.putAll(
                    currencies
                        .associateWith { 0.0 }
                        .filter { !savedBalances.contains(it.key) }
                )
                //save updated map
                prefs[USER_BALANCES] = adapter.toJson(savedBalances)

            } ?: run {
                /**
                First time initializing
                 */
                val balancesMap = currencies.associateWith { 0.0 }.toMutableMap()
                balancesMap[primaryCurrency] = initialAmount
                prefs[USER_BALANCES] = adapter.toJson(balancesMap)
            }
        }
    }

    override fun getBalance(): Flow<UserBalanceModel> {
        return context.dataStore.data
            .map { prefs ->
                prefs[USER_BALANCES]?.let {
                    UserBalanceModel(currencies = adapter.fromJson(it) as MutableMap<String, Double>)
                } ?: UserBalanceModel()
            }
    }

    override suspend fun exchangeCurrency(
        fromCurrency: String,
        sellAmount: String,
        toCurrency: String,
        buyAmount: String
    ): TransactionResponse {
        if (sellAmount.isValidDouble() && buyAmount.isValidDouble()) {
            context.dataStore.edit { prefs ->
                prefs[USER_BALANCES]?.let {
                    //fetch map
                    val savedBalances: MutableMap<String, Double> =
                        adapter.fromJson(it) as MutableMap<String, Double>

                    //make transactions
                    savedBalances[fromCurrency]?.let { currentAmount ->
                        savedBalances[fromCurrency] = currentAmount - sellAmount.toDouble()
                    }
                    savedBalances[toCurrency]?.let { currentAmount ->
                        savedBalances[toCurrency] = currentAmount + buyAmount.toDouble()
                    }

                    //save updated map
                    prefs[USER_BALANCES] = adapter.toJson(savedBalances)
                }
            }
        } else {
            return TransactionResponse.Error(error = TransactionError.INVALID_AMOUNT)
        }
        return TransactionResponse.Success
    }

}