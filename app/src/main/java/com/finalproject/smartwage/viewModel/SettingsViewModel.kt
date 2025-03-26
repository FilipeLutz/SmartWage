package com.finalproject.smartwage.viewModel

import android.annotation.SuppressLint
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

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _notificationsEnabled = MutableStateFlow(false)

    private val _darkModeEnabled = MutableStateFlow(false)
    val darkModeEnabled: StateFlow<Boolean> = _darkModeEnabled.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                settingsRepository.getUserSettings(userId)?.let { settings ->
                    _notificationsEnabled.value = settings.notificationsEnabled
                    _darkModeEnabled.value = settings.darkModeEnabled
                }
            } catch (e: Exception) {
                Timber.e(e, "Error loading settings")
            }
        }
    }

    fun updateDarkModeEnabled(enabled: Boolean) {
        _darkModeEnabled.value = enabled
        saveSettings()
    }

    private fun saveSettings() {
        viewModelScope.launch {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                settingsRepository.saveUserSettings(
                    userId,
                    Settings(
                        userId = userId,
                        notificationsEnabled = _notificationsEnabled.value,
                        darkModeEnabled = _darkModeEnabled.value
                    )
                )
            } catch (e: Exception) {
                Timber.e(e, "Error saving settings")
            }
        }
    }
}