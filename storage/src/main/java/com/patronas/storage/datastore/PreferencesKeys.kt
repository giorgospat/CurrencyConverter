package com.patronas.storage.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val USER_BALANCES = stringPreferencesKey("user_balances")
    val TRANSACTION_DATES = stringPreferencesKey("transaction_dates")
}
