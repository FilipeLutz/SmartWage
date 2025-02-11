package com.finalproject.smartwage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.model.User
import com.finalproject.smartwage.data.repository.UserRepository
import com.finalproject.smartwage.utils.UserMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepo: UserRepository
) : ViewModel() {

    fun updateUser(remoteUser: User) {
        viewModelScope.launch {
            val localUser = UserMapper.remoteToLocal(remoteUser) // Convert to Room User
            userRepo.saveUser(localUser)
        }
    }

    fun deleteUser(userId: String) {
        viewModelScope.launch {
            userRepo.logout()
        }
    }
}