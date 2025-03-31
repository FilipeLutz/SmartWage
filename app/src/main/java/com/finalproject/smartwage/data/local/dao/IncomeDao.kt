package com.finalproject.smartwage.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.finalproject.smartwage.data.local.entities.Income
import kotlinx.coroutines.flow.Flow

/**
 * IncomeDao is an interface that defines the data access methods for the Income entity.
 * It provides methods to insert, retrieve, and delete income records from the database.
 *
 * @Dao annotation indicates that this is a Data Access Object.
 */

@Dao
// Data Access Object for Income
interface IncomeDao {
    // Insert income
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncome(income: Income)
    // Get user income
    @Query("SELECT * FROM income WHERE userId = :userId")
    fun getUserIncome(userId: String): Flow<List<Income>>
    // Delete income
    @Query("DELETE FROM income WHERE id = :incomeId")
    suspend fun deleteIncome(incomeId: String)
}