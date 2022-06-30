package com.patronas.currencyconverter.presentation.extensions

fun String.isValidAmount(): Boolean {
    return this.toDoubleOrNull() != null || this.isEmpty()
}

fun String.isValidDouble(): Boolean {
    return this.toDoubleOrNull() != null
}

fun String.hasValidBalance(balance: Double, fee: Double): Boolean {
    return this.isValidDouble() && this.toDouble().plus(fee) <= balance
}

fun String.toDoubleOrDefault(): Double {
    return this.toDoubleOrNull() ?: 0.0
}