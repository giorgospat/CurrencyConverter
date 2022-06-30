package com.patronas.currencyconverter.presentation.extensions

import java.util.*

fun Double.round(decimals: Int = 2): String {
    return String.format(Locale.US, "%.${decimals}f", this)
}

fun Double.convertToBaseCurrency(rate: Double): Double {
    return this * rate
}