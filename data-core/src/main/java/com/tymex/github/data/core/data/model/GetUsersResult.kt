package com.tymex.github.data.core.data.model

data class GetUsersResult(
    val users: List<User>,
    val isFinalPage: Boolean = false,
    var isDirty: Boolean = false
)
