package com.finalproject.smartwage.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.finalproject.smartwage.data.local.entities.User
import kotlinx.coroutines.flow.Flow

/**
 * UserDao is an interface that defines the data access methods for the User entity.
 * It uses Room annotations to define the database operations.
 *
 * @Dao annotation indicates that this is a Data Access Object.
 */

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