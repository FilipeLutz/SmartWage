package com.finalproject.smartwage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.local.entities.Settings
import com.finalproject.smartwage.data.repository.SettingsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for managing user settings.
 *
 * @property settingsRepository The repository for accessing user settings.
 */

@HiltViewModel
class SettingsViewModel @Inject constructor(
    // Inject the SettingsRepository dependency
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    // MutableStateFlow to hold the notifications enabled state
    private val _notificationsEnabled = MutableStateFlow(false)

    // MutableStateFlow to hold the dark mode enabled state
    private val _darkModeEnabled = MutableStateFlow(false)
    val darkModeEnabled: StateFlow<Boolean> = _darkModeEnabled.asStateFlow()

    // Initializer to load settings when the ViewModel is created
    init {
        loadSettings()
    }

    // Load user settings from the repository
    private fun loadSettings() {
        viewModelScope.launch {
            try {
                // Get the current user's ID from Firebase Authentication
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                settingsRepository.getUserSettings(userId)?.let { settings ->
                    // Update the MutableStateFlow values with the loaded settings
                    _notificationsEnabled.value = settings.notificationsEnabled
                    // Update the dark mode enabled state
                    _darkModeEnabled.value = settings.darkModeEnabled
                }
            } catch (e: Exception) {
                // Timber any errors that occur during loading
                Timber.e(e, "Error loading settings")
            }
        }
    }

    // Function to update dark mode enabled state
    fun updateDarkModeEnabled(enabled: Boolean) {
        // Update the MutableStateFlow value
        _darkModeEnabled.value = enabled
        // Save the updated settings
        saveSettings()
    }

    // Function to save user settings
    private fun saveSettings() {
        viewModelScope.launch {
            try {
                // Get the current user's ID from Firebase Authentication
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                settingsRepository.saveUserSettings(
                    // Save the settings using the repository
                    userId,
                    Settings(
                        userId = userId,
                        notificationsEnabled = _notificationsEnabled.value,
                        darkModeEnabled = _darkModeEnabled.value
                    )
                )
            } catch (e: Exception) {
                // Timber any errors that occur during saving
                Timber.e(e, "Error saving settings")
            }
        }
    }
}