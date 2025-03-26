package com.finalproject.smartwage.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.finalproject.smartwage.data.local.entities.Settings

@Dao
// Data Access Object for Settings
interface SettingsDao {
    // Get user settings
    @Query("SELECT * FROM settings WHERE userId = :userId LIMIT 1")
    suspend fun getSettings(userId: String): Settings?
    // Save user settings
    @Insert
    suspend fun saveSettings(settings: Settings)
    // Update user settings
    @Update
    suspend fun updateSettings(settings: Settings)
}