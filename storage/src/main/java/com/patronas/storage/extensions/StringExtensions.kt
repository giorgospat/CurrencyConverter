package com.patronas.storage.extensions

fun String.isValidDouble(): Boolean {
    return this.toDoubleOrNull() != null
}