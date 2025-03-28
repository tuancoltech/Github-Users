package com.tymex.github.data.core.viewmodel

import com.tymex.github.data.core.data.model.FlowState
import kotlinx.coroutines.flow.Flow

interface UserViewModel {
    fun getUsers(pageSize: Int, page: Int): Flow<FlowState>
    fun getUserDetails(login: String): Flow<FlowState>
}