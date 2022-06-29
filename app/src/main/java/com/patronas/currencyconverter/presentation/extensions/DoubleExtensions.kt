package com.patronas.currencyconverter.presentation.extensions

fun Double.round(decimals: Int = 2): String {
    return String.format("%.${decimals}f", this)
}