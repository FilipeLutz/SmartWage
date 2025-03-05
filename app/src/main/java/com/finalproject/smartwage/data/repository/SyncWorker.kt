package com.finalproject.smartwage.data.repository

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import timber.log.Timber

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val incomeRepo: IncomeRepository
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            incomeRepo.syncOfflineData()
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "Error syncing offline data")
            Result.retry()
        }
    }
}