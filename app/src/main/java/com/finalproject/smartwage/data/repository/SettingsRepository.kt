package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.SettingsDao
import com.finalproject.smartwage.data.local.entities.Settings
import com.finalproject.smartwage.data.remote.FirestoreService
import timber.log.Timber
import javax.inject.Inject

/**
 * SettingsRepository is responsible for managing user settings.
 * It interacts with Firestore and Room to fetch and save settings.
 *
 * @param firestoreService The Firestore service for remote data operations.
 * @param settingsDao The Room DAO for local data operations.
 */

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
        try {
            firestoreService.saveUserSettings(userId, settings)
        } catch (e: Exception) {
            Timber.e(e, "Error saving user settings to Firestore")
        }
    }
}