package com.finalproject.smartwage.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// Settings Entity
@Entity(tableName = "settings")
// Data class for Settings
data class Settings(
    // Primary key
    @PrimaryKey val userId: String,
    val notificationsEnabled: Boolean = true,
    val selectedLanguage: String = "en",
    val darkModeEnabled: Boolean = false
)
