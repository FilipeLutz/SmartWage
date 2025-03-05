package com.finalproject.smartwage.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.finalproject.smartwage.data.local.entities.Income
import com.finalproject.smartwage.data.repository.IncomeRepository
import com.finalproject.smartwage.data.repository.SyncWorker
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class IncomeViewModel @Inject constructor(
    private val incomeRepo: IncomeRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _userIncomes = MutableStateFlow<List<Income>>(emptyList())
    val userIncomes: StateFlow<List<Income>> = _userIncomes.asStateFlow()

    init {
        loadIncomes()
    }

    fun loadIncomes() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                incomeRepo.getUserIncomes().collect { incomes ->
                    _userIncomes.value = incomes
                }
            }
        } else {
            Timber.e("No logged-in user found. Cannot load incomes.")
        }
    }

    fun updateIncome(income: Income) {
        viewModelScope.launch {
            incomeRepo.saveOrUpdateIncome(income)
            loadIncomes()
        }
    }

    fun deleteIncome(incomeId: String, userId: String) {
        viewModelScope.launch {
            incomeRepo.deleteIncome(incomeId)
            loadIncomes()
        }
    }

    fun scheduleOfflineSync(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "SyncWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}