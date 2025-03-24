package com.finalproject.smartwage.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.finalproject.smartwage.data.local.entities.Settings

@Dao
interface SettingsDao {

    @Query("SELECT * FROM settings WHERE userId = :userId LIMIT 1")
    suspend fun getSettings(userId: String): Settings?

    @Insert
    suspend fun saveSettings(settings: Settings)

    @Update
    suspend fun updateSettings(settings: Settings)
}