package com.tymex.github.users.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.tymex.github.data.core.data.model.User
import com.tymex.github.users.databinding.RowUserBinding

class UserAdapter(
    private val onUserClicked: ((User) -> Unit)? = null
) : ListAdapter<User, RecyclerView.ViewHolder>(UserDiffCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = currentList[position]
        (holder as UserHolder).bindItem(user)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return UserHolder(
            RowUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onUserClicked
        )
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    fun setData(newUsers: List<User>) {
        submitList(newUsers.distinctBy { it.login })
    }

    fun appendUsers(newUsers: List<User>) {
        val updatedUsers = currentList + newUsers
        setData(updatedUsers)
    }

    class UserHolder(
        private val binding: RowUserBinding,
        private val onUserClicked: ((User) -> Unit)? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(user: User) {
            binding.ivAvatar.load(user.avatarUrl)
            binding.tvName.text = user.login
            binding.tvUrl.text = user.htmlUrl
            binding.root.setOnClickListener {
                onUserClicked?.invoke(user)
            }
        }
    }
}