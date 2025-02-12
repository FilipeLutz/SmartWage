package com.finalproject.smartwage.data.local.dao

import androidx.room.*
import com.finalproject.smartwage.data.local.entities.Income
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncome(income: Income)

    @Query("SELECT * FROM income WHERE userId = :userId")
    fun getUserIncome(userId: String): Flow<List<Income>>

    @Query("DELETE FROM income WHERE id = :incomeId")
    suspend fun deleteIncome(incomeId: String)
}