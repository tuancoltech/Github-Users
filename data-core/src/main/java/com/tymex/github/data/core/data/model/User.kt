package com.tymex.github.data.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "users")
@JsonClass(generateAdapter = true)
data class User(

    @Json(name = "id")
    @PrimaryKey var id: Long = 0L,

    @Json(name = "login")
    var login: String? = null,

    @Json(name = "avatar_url")
    var avatarUrl: String? = null,

    @Json(name = "html_url")
    var htmlUrl: String? = null,
)
