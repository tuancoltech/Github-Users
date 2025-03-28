package com.tymex.github.data.core.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDetails(

    @Json(name = "login")
    var login: String? = null,

    @Json(name = "avatar_url")
    var avatarUrl: String? = null,

    @Json(name = "blog")
    var blog: String? = null,

    @Json(name = "location")
    var location: String? = null,

    @Json(name = "followers")
    var followers: Long = 0,

    @Json(name = "following")
    var following: Long = 0,
)
