package com.patronas.currencyconverter.presentation.extensions

fun Double.round(): String {
    return String.format("%.2f", this)
}