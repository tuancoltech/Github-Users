package com.tymex.github.data.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IoScope

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class MainScope

@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopeModule {
    @Singleton
    @Provides
    @IoScope
    fun provideIoScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)

    @Singleton
    @Provides
    @MainScope
    fun provideMainScope(): CoroutineScope = CoroutineScope(Dispatchers.Main)
}