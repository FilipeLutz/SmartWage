package com.finalproject.smartwage.data.local.dao

import androidx.room.*
import com.finalproject.smartwage.data.local.entities.Expenses
import kotlinx.coroutines.flow.Flow

@Dao
// Data Access Object for Expenses
interface ExpenseDao {
    // Insert expenses
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenses(expenses: Expenses)
    // Get user expenses
    @Query("SELECT * FROM expenses WHERE userId = :userId")
    fun getUserExpenses(userId: String): Flow<List<Expenses>>
    // Delete expenses
    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun deleteExpenses(expenseId: String)
}