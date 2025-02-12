package com.finalproject.smartwage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.local.entities.User
import com.finalproject.smartwage.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepo: UserRepository
) : ViewModel() {

    fun updateUser(remoteUser: User) {
        viewModelScope.launch {
            userRepo.saveUser(remoteUser)
        }
    }

    fun deleteUser(userId: String) {
        viewModelScope.launch {
            userRepo.logout()
        }
    }
}