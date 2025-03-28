package com.tymex.github.data.core

import app.cash.turbine.test
import com.tymex.github.data.core.data.model.FlowState
import com.tymex.github.data.core.data.model.GetUsersResult
import com.tymex.github.data.core.data.model.User
import com.tymex.github.data.core.data.repository.users.UserRepository
import com.tymex.github.data.core.data.repository.users.local.UserLocalDataSource
import com.tymex.github.data.core.data.repository.users.remote.UserRemoteDataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    @Mock
    lateinit var localSource: UserLocalDataSource

    @Inject
    @Mock
    lateinit var remoteSource: UserRemoteDataSource

    @Mock
    private lateinit var insertUsers: (List<User>) -> Unit

    @Inject
    lateinit var repository: UserRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        hiltRule.inject() // Inject Hilt dependencies before running tests
    }

    @Test
    fun `when local data is available, should return local users`() = runTest {
        val users = listOf(User(id = 1, login = "Alice"))
        val localData = flowOf(FlowState.Success(GetUsersResult(users)))

        `when`(localSource.getUsers(20, 1)).thenReturn(localData)

        repository.getUsers(20, 1).test {
            assertEquals(FlowState.Success(GetUsersResult(users)), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when local data is empty, should fetch from remote and insert`() = runTest {
        val emptyLocalData = flowOf(FlowState.Success(GetUsersResult(emptyList())))
        val remoteUsers = listOf(User(id = 2, login = "Bob"))
        val remoteData = flowOf(FlowState.Success(GetUsersResult(remoteUsers)))

        `when`(localSource.getUsers(20, 1)).thenReturn(emptyLocalData)
        `when`(remoteSource.getUsers(20, 1)).thenReturn(remoteData)

        repository.getUsers(20, 1).test {
            assertEquals(FlowState.Success(GetUsersResult(remoteUsers)), awaitItem())
            verify(insertUsers).invoke(remoteUsers)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when local data is error, should fetch from remote`() = runTest {
        val errorState = flowOf(FlowState.Error("Database error"))
        val remoteUsers = listOf(User(id = 3, login = "Charlie"))
        val remoteData = flowOf(FlowState.Success(GetUsersResult(remoteUsers)))

        `when`(localSource.getUsers(20, 1)).thenReturn(errorState)
        `when`(remoteSource.getUsers(20, 1)).thenReturn(remoteData)

        repository.getUsers(20, 1).test {
            assertEquals(FlowState.Success(GetUsersResult(remoteUsers)), awaitItem())
            verify(insertUsers).invoke(remoteUsers)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should return loading state`() = runTest {
        val loadingState = flowOf(FlowState.Loading)
        `when`(localSource.getUsers(20, 1)).thenReturn(loadingState)

        repository.getUsers(20, 1).test {
            assertEquals(FlowState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
