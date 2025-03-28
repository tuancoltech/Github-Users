package com.tymex.github.data.core.data.repository.users.remote

import com.tymex.github.data.core.data.model.FlowState
import kotlinx.coroutines.flow.Flow

interface UserRemoteDataSource {
    fun getUsers(pageSize: Int, page: Int): Flow<FlowState>
    fun getUserDetails(login: String): Flow<FlowState>
}