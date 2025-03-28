//package com.tymex.github.data.core.di
//
//import com.tymex.github.data.core.data.model.FlowState
//import com.tymex.github.data.core.data.model.User
//import com.tymex.github.data.core.data.repository.users.local.UserLocalDataSource
//import com.tymex.github.data.core.data.repository.users.local.UserLocalDataSourceImpl
//import dagger.Binds
//import dagger.Module
//import dagger.hilt.components.SingletonComponent
//import dagger.hilt.testing.TestInstallIn
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flowOf
//import javax.inject.Inject
//
//@Module
//@TestInstallIn(
//    components = [SingletonComponent::class], // Install in the same component as AppModule
//    replaces = [DataModule::class] // Replace original module
//)
//abstract class TestDataModule {
//
//    @Binds
//    abstract fun bindFakeUserRepository(
//        impl: FakeUserLocalDataSource
//    ): UserLocalDataSource
//}
//
//// Fake implementation for testing
//class FakeUserLocalDataSource @Inject constructor() : UserLocalDataSource {
//    override suspend fun getUsers(pageSize: Int, page: Int): Flow<FlowState> {
//        return flowOf(FlowState.Success(data = listOf(User(1), User(id = 2))))
//    }
//
//    override fun insertUsers(users: List<User>) {
//    }
//}