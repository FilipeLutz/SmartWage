package com.finalproject.smartwage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.repository.AuthRepository
import com.finalproject.smartwage.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(

) : ViewModel() {

}