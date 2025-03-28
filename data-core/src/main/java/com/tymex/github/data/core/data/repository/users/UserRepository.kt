package com.tymex.github.data.core.data.repository.users

import com.tymex.github.data.core.data.model.FlowState
import com.tymex.github.data.core.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(pageSize: Int, page: Int): Flow<FlowState>
    fun getUserDetails(login: String): Flow<FlowState>
    fun insertUsers(users: List<User>)
}