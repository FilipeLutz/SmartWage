package com.finalproject.smartwage.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.finalproject.smartwage.data.local.entities.Settings

/**
 * SettingsDao is an interface that defines the data access methods for the Settings entity.
 * It provides methods to get, save, and update user settings in the local database.
 *
 * @Dao annotation indicates that this is a Data Access Object.
 */

@Dao
// Data Access Object for Settings
interface SettingsDao {
    // Get user settings
    @Query("SELECT * FROM settings WHERE userId = :userId LIMIT 1")
    suspend fun getSettings(userId: String): Settings?
    // Save user settings
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: Settings)
    // Update user settings
    @Update(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun updateSettings(settings: Settings)
}