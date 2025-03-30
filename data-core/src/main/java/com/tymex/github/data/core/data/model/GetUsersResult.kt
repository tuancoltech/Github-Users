package com.tymex.github.data.core.data.model

/**
 * A wrapper of users list returned from a data repository
 * [users] List of users
 * [isFinalPage] true if this is the final page of data available, false otherwise
 */
data class GetUsersResult(
    val users: List<User>,
    val isFinalPage: Boolean = false,
)
