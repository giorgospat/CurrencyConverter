package com.patronas.utils

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    fun main(): CoroutineDispatcher
    fun background(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
}

class DispatcherProviderImpl(
    private val mainThread: CoroutineDispatcher,
    private val backgroundThread: CoroutineDispatcher,
    private val default: CoroutineDispatcher
) : DispatcherProvider {
    override fun main(): CoroutineDispatcher = mainThread

    override fun background(): CoroutineDispatcher = backgroundThread

    override fun default(): CoroutineDispatcher = default

}