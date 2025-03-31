package com.finalproject.smartwage.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Settings is a data class that represents the settings of the application.
 * It includes properties such as userId, notificationsEnabled,
 * selectedLanguage, and darkModeEnabled.
 */

@Entity(tableName = "settings")
// Data class for Settings
data class Settings(
    // Primary key
    @PrimaryKey
    val userId: String,
    val notificationsEnabled: Boolean = true,
    val selectedLanguage: String = "en",
    val darkModeEnabled: Boolean = false
)
