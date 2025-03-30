package com.tymex.github.data.core.data.repository.users.remote

import android.util.Log
import com.tymex.github.data.core.data.ext.isLastPage
import com.tymex.github.data.core.data.ext.toNetworkResponse
import com.tymex.github.data.core.data.model.FlowState
import com.tymex.github.data.core.data.model.GetUsersResult
import com.tymex.github.data.core.data.model.NetworkResponse
import com.tymex.github.data.core.data.model.User
import com.tymex.github.data.core.data.model.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val userService: UserService
) : UserRemoteDataSource {

    override fun getUsers(pageSize: Int, page: Int): Flow<FlowState> {
        return flow {
            emit(FlowState.Loading)

            var isFinalPage = false
            val since = (page - 1) * pageSize
            Log.d(TAG, "getUsers page: $page. pageSize: $pageSize. since: $since")
            val networkResponse: NetworkResponse<List<User>> = try {
                val response = userService.getUsers(pageSize, since)
                isFinalPage = response.headers()["Link"]?.isLastPage() ?: false
                response.toNetworkResponse()
            } catch (ex: Exception) {
                ex.toNetworkResponse()
            }


            when (networkResponse) {
                is NetworkResponse.Success -> emit(
                    FlowState.Success(data = GetUsersResult(
                        users = networkResponse.data, isFinalPage = isFinalPage)
                    )
                )

                is NetworkResponse.Error -> emit(
                    FlowState.Error(networkResponse.message)
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getUserDetails(login: String): Flow<FlowState> {
        return flow {
            emit(FlowState.Loading)

            Log.i(TAG, "getUserDetails login: $login")
            val networkResponse: NetworkResponse<UserDetails> = try {
                userService.getUserDetails(login).toNetworkResponse()
            } catch (ex: Exception) {
                ex.toNetworkResponse()
            }

            when (networkResponse) {
                is NetworkResponse.Success -> emit(
                    FlowState.Success(data = networkResponse.data)
                )

                is NetworkResponse.Error -> emit(
                    FlowState.Error(networkResponse.message)
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    companion object {
        private val TAG by lazy { UserRemoteDataSource::class.java.name }
    }
}