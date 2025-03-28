package com.tymex.github.data.core.data.repository.users.remote

import com.tymex.github.data.core.data.model.User
import com.tymex.github.data.core.data.model.UserDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    @GET("/users")
    suspend fun getUsers(@Query("per_page") pageSize: Int,
                         @Query("since") since: Int): Response<List<User>>

    @GET("/users/{login}")
    suspend fun getUserDetails(@Path("login") login: String): Response<UserDetails>
}