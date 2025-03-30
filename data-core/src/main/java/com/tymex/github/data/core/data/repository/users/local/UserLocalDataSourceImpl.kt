package com.tymex.github.data.core.data.repository.users.local

import android.util.Log
import com.tymex.github.data.core.data.model.FlowState
import com.tymex.github.data.core.data.model.GetUsersResult
import com.tymex.github.data.core.data.model.User
import com.tymex.github.data.core.di.IoScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao,
    @IoScope private val coroutineScope: CoroutineScope,
) : UserLocalDataSource {

    override fun getUsers(pageSize: Int, page: Int): Flow<FlowState> {
        return flow {
            emit(FlowState.Loading)
            val since = (page - 1) * pageSize
            Log.d(TAG,
                "getUsers page: $page. pageSize: $pageSize. since: $since"
            )
            try {
                val response = userDao.getUsers(pageSize, since)
                emit(
                    FlowState.Success(
                        data = GetUsersResult(
                            users = response, isFinalPage = response.isEmpty()
                        )
                    )
                )
            } catch (ex: Exception) {
                emit(FlowState.Error(ex.message))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun insertUsers(users: List<User>) {
        coroutineScope.launch {
            Log.v(TAG, "insertUsers : $users\nSize: $users.size)")
            userDao.insertUsers(users)
        }
    }

    companion object {
        private val TAG by lazy { UserLocalDataSourceImpl::class.java.name }
    }
}