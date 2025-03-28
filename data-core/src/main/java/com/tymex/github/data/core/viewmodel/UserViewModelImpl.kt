package com.tymex.github.data.core.viewmodel

import androidx.lifecycle.ViewModel
import com.tymex.github.data.core.data.model.FlowState
import com.tymex.github.data.core.data.repository.users.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class UserViewModelImpl @Inject constructor(private val userRepository: UserRepository) :
    UserViewModel, ViewModel() {

    override fun getUsers(pageSize: Int, page: Int): Flow<FlowState> {
        return userRepository.getUsers(pageSize, page)
    }

    override fun getUserDetails(login: String): Flow<FlowState> {
        return userRepository.getUserDetails(login)
    }
}