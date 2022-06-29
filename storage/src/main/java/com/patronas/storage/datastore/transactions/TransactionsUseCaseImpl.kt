package com.patronas.storage.datastore.transactions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.patronas.storage.model.UserBalanceModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.reflect.Type


class TransactionsUseCaseImpl(val context: Context, val moshi: Moshi) : TransactionsUseCase {

    private val userBalance = UserBalanceModel()
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
            Already initilized with initial amount
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

    override suspend fun getBalance(): Flow<UserBalanceModel> {
        return context.dataStore.data
            .map { prefs ->
                prefs[USER_BALANCES]?.let {
                    UserBalanceModel(currencies = adapter.fromJson(it) as MutableMap<String, Double>)
                } ?: UserBalanceModel()
            }
    }

    override suspend fun updateBalance(currency: String, amount: Double) {
        context.dataStore.edit { prefs ->
            prefs[USER_BALANCES]?.let {
                //fetch map
                val savedBalances: MutableMap<String, Double> =
                    adapter.fromJson(it) as MutableMap<String, Double>
                savedBalances[currency] = amount

                //save updated map
                prefs[USER_BALANCES] = adapter.toJson(savedBalances)
            }
        }
    }

}