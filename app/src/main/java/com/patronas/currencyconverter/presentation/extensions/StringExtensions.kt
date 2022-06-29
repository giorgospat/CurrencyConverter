package com.patronas.currencyconverter.presentation.extensions

fun String.isValidAmount(): Boolean {
    return this.toDoubleOrNull() != null || this.isEmpty()
}