package com.finalproject.smartwage.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey val userId: String,
    val notificationsEnabled: Boolean = true,
    val selectedLanguage: String = "en",
    val darkModeEnabled: Boolean = false
)
