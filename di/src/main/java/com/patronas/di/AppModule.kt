package com.patronas.di

import com.patronas.utils.DateProvider
import com.patronas.utils.DateProviderImpl
import com.patronas.utils.DispatcherProvider
import com.patronas.utils.DispatcherProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider {
        return DispatcherProviderImpl(
            mainThread = Dispatchers.Main,
            backgroundThread = Dispatchers.IO,
            default = Dispatchers.Default
        )
    }

    @Provides
    @Singleton
    fun provideDateProvider(): DateProvider {
        return DateProviderImpl()
    }

}