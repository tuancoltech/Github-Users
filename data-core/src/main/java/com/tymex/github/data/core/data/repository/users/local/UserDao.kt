package com.tymex.github.data.core.data.repository.users.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tymex.github.data.core.data.model.User

@Dao
interface UserDao {

    // Fetch paginated users
    @Query("SELECT * FROM users ORDER BY id ASC LIMIT :pageSize OFFSET :offset")
    suspend fun getUsers(pageSize: Int, offset: Int): List<User>

    // Insert users (replace if already exists)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(items: List<User>)

    // Count total users in the database
    @Query("SELECT COUNT(*) FROM users")
    suspend fun getTotalItemCount(): Long
}