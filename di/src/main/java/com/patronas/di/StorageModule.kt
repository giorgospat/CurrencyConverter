package com.patronas.di

import android.content.Context
import com.patronas.storage.datastore.transactions.TransactionsUseCase
import com.patronas.storage.datastore.transactions.TransactionsUseCaseImpl
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Provides
    @Singleton
    fun provideTransactionsUseCase(@ApplicationContext context: Context, moshi: Moshi): TransactionsUseCase {
        return TransactionsUseCaseImpl(context, moshi)
    }
}