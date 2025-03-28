package com.tymex.github.data.core.data.repository.users.local

import com.tymex.github.data.core.data.model.FlowState
import com.tymex.github.data.core.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    fun getUsers(pageSize: Int, page: Int): Flow<FlowState>
    fun insertUsers(users: List<User>)
}