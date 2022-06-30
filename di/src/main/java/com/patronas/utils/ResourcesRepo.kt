package com.patronas.utils

import android.content.Context
import androidx.annotation.StringRes

interface ResourcesRepo {
    fun getString(@StringRes resId: Int): String
    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String
}

class ResourcesRepoImpl(private val context: Context) : ResourcesRepo {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }
}