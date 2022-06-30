package com.patronas.utils

import java.util.*

interface DateProvider {
    fun getCurrentDate(): Date
}

class DateProviderImpl : DateProvider {

    override fun getCurrentDate(): Date {
        return Calendar.getInstance().time
    }

}