package com.patronas.di

import android.content.Context
import com.patronas.utils.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideResourcesRepo(@ApplicationContext context: Context): ResourcesRepo {
        return ResourcesRepoImpl(context = context)
    }

}