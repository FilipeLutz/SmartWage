package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.SettingsDao
import com.finalproject.smartwage.data.local.entities.Settings
import com.finalproject.smartwage.data.remote.FirestoreService
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val firestoreService: FirestoreService,
    private val settingsDao: SettingsDao
) {

    // Get settings from Firestore or Room
    suspend fun getUserSettings(userId: String): Settings? {
        return firestoreService.getUserSettings(userId) ?: settingsDao.getSettings(userId)
    }

    // Save settings to Firestore and Room
    suspend fun saveUserSettings(userId: String, settings: Settings) {
        firestoreService.saveUserSettings(userId, settings)
        settingsDao.saveSettings(settings)
    }

    // Update settings in Firestore and Room
    suspend fun updateUserSettings(userId: String, settings: Settings) {
        firestoreService.updateUserSettings(userId, settings)
        settingsDao.updateSettings(settings)
    }
}