package com.patronas.domain.extensions

import java.util.*

fun Date.day(): Int {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.DAY_OF_MONTH)
}