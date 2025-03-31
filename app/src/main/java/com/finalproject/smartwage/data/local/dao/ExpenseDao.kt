package com.finalproject.smartwage.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.finalproject.smartwage.data.local.entities.Expenses
import kotlinx.coroutines.flow.Flow

/**
 * ExpenseDao is an interface that defines the data access methods for the Expenses entity.
 * It provides methods to insert, retrieve, and delete expenses from the database.
 *
 * @Dao annotation indicates that this is a Data Access Object.
 */

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