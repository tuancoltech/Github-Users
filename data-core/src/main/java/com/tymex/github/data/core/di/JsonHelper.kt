package com.tymex.github.data.core.di

import com.squareup.moshi.Moshi
import javax.inject.Inject

class JsonHelper @Inject constructor(private val moshi: Moshi) {

    fun <T : Any> toObject(strJson: String?, clazz: Class<T>): T? {
        return if (!strJson.isNullOrEmpty()) {
            val adapter = moshi.adapter(clazz)
            adapter.fromJson(strJson)
        } else {
            null
        }
    }
}