package com.finalproject.smartwage.data.local.dao

import androidx.room.*
import com.finalproject.smartwage.data.local.entities.Income

@Dao
interface IncomeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncome(income: Income)

    @Query("SELECT * FROM income WHERE userId = :userId")
    suspend fun getUserIncome(userId: String): List<Income>

    @Query("DELETE FROM income WHERE id = :incomeId")
    suspend fun deleteIncome(incomeId: String)
}