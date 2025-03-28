package com.tymex.github.data.core.di

import com.tymex.github.data.core.data.repository.users.UserRepository
import com.tymex.github.data.core.data.repository.users.UserRepositoryImpl
import com.tymex.github.data.core.data.repository.users.local.UserLocalDataSource
import com.tymex.github.data.core.data.repository.users.local.UserLocalDataSourceImpl
import com.tymex.github.data.core.data.repository.users.remote.UserRemoteDataSource
import com.tymex.github.data.core.data.repository.users.remote.UserRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindUserRepository(repo: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindUserRemoteDataSource(dataSource: UserRemoteDataSourceImpl): UserRemoteDataSource

    @Binds
    abstract fun bindUserLocalDataSource(dataSource: UserLocalDataSourceImpl): UserLocalDataSource
}