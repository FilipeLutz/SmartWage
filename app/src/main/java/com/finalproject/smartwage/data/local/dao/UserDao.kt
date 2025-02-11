package com.finalproject.smartwage.data.local.dao

import androidx.room.*
import com.finalproject.smartwage.data.local.entities.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): User?

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int
}