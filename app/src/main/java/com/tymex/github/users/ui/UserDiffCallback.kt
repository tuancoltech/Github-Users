package com.tymex.github.users.ui

import androidx.recyclerview.widget.DiffUtil
import com.tymex.github.data.core.data.model.User

/**
 * DiffUtil implementation for efficient updates.
 */
class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id== newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return (oldItem.login == newItem.login)
                && (oldItem.avatarUrl == newItem.avatarUrl)
                && (oldItem.htmlUrl == newItem.htmlUrl)
    }
}