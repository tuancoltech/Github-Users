package com.tymex.github.data.core.data.repository.users

import android.util.Log
import com.tymex.github.data.core.data.model.FlowState
import com.tymex.github.data.core.data.model.GetUsersResult
import com.tymex.github.data.core.data.model.User
import com.tymex.github.data.core.data.repository.users.local.UserLocalDataSource
import com.tymex.github.data.core.data.repository.users.remote.UserRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteSource: UserRemoteDataSource,
    private val localSource: UserLocalDataSource
) : UserRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUsers(pageSize: Int, page: Int): Flow<FlowState> {
        return localSource.getUsers(pageSize, page).flatMapLatest { localUsersFlow ->
            when (localUsersFlow) {
                is FlowState.Success<*> -> {
                    val localUsers = (localUsersFlow.data as? GetUsersResult)?.users ?: emptyList()
                    Log.v("tuancoltech", "Got localUsers with size: " + localUsers.size)
                    if (localUsers.isNotEmpty()) {
                        flowOf(localUsersFlow)
                    } else {
                        remoteSource.getUsers(pageSize, page).onEach {
                            if (it is FlowState.Success<*>) {
                                val remoteUsers = (it.data as? GetUsersResult)?.users ?: emptyList()
                                insertUsers(remoteUsers)
                            }
                        }
                    }
                }

                is FlowState.Error -> {
                    remoteSource.getUsers(pageSize, page).onEach {
                        if (it is FlowState.Success<*>) {
                            val remoteUsers = (it.data as? GetUsersResult)?.users ?: emptyList()
                            insertUsers(remoteUsers)
                        }
                    }
                }

                is FlowState.Loading -> flowOf(FlowState.Loading)
            }
        }
    }

    override fun insertUsers(users: List<User>) {
        localSource.insertUsers(users)
    }

    override fun getUserDetails(login: String): Flow<FlowState> {
        return remoteSource.getUserDetails(login)
    }
}