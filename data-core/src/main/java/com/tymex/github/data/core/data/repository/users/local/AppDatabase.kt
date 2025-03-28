package com.tymex.github.data.core.data.repository.users.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tymex.github.data.core.data.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
