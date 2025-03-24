package com.finalproject.smartwage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.local.entities.Settings
import com.finalproject.smartwage.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled

    private val _selectedLanguage = MutableStateFlow("en")
    val selectedLanguage: StateFlow<String> = _selectedLanguage

    private val _darkModeEnabled = MutableStateFlow(false)
    val darkModeEnabled: StateFlow<Boolean> = _darkModeEnabled

    // Load settings from the repository and update state
    fun loadUserSettings(userId: String) {
        viewModelScope.launch {
            val settings = settingsRepository.getUserSettings(userId)

            _notificationsEnabled.value = settings?.notificationsEnabled != false
            _selectedLanguage.value = settings?.selectedLanguage ?: "en"
            _darkModeEnabled.value = settings?.darkModeEnabled == true
        }
    }

    // Save settings to the repository
    fun saveUserSettings(userId: String) {
        val settings = Settings(
            userId = userId,
            notificationsEnabled = _notificationsEnabled.value,
            selectedLanguage = _selectedLanguage.value,
            darkModeEnabled = _darkModeEnabled.value
        )
        viewModelScope.launch {
            settingsRepository.saveUserSettings(userId, settings)
        }
    }

    // Update settings in the repository
    fun updateUserSettings(userId: String) {
        val settings = Settings(
            userId = userId,
            notificationsEnabled = _notificationsEnabled.value,
            selectedLanguage = _selectedLanguage.value,
            darkModeEnabled = _darkModeEnabled.value
        )
        viewModelScope.launch {
            settingsRepository.updateUserSettings(userId, settings)
        }
    }
}