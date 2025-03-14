package com.finalproject.smartwage.data.local.dao

import androidx.room.*
import com.finalproject.smartwage.data.local.entities.Expenses
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenses(expenses: Expenses)

    @Query("SELECT * FROM expenses WHERE userId = :userId")
    fun getUserExpenses(userId: String): Flow<List<Expenses>>

    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun deleteExpenses(expenseId: String)
}