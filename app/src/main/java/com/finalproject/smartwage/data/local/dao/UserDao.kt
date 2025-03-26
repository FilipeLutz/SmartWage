package com.finalproject.smartwage.data.local.dao

import androidx.room.*
import com.finalproject.smartwage.data.local.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
// Data Access Object for User
interface UserDao {
    // Insert user
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
    // Get user by id
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: String): Flow<User?>
    // Delete user
    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)
}