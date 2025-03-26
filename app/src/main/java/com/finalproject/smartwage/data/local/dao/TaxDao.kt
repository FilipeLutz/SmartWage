package com.finalproject.smartwage.data.local.dao

import androidx.room.*
import com.finalproject.smartwage.data.local.entities.Tax
import kotlinx.coroutines.flow.Flow

@Dao
// Data Access Object for Tax
interface TaxDao {
    // Insert tax
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTax(tax: Tax)
    // Get user tax
    @Query("SELECT * FROM tax WHERE userId = :userId")
    fun getUserTax(userId: String): Flow<List<Tax>>
    // Delete tax
    @Query("DELETE FROM tax WHERE userId = :userId")
    suspend fun deleteTax(userId: String)
    // Get tax credits
    @Query("SELECT rentTaxCredit FROM Tax WHERE userId = :userId")
    fun getRentTaxCredit(userId: String): Flow<Double>
    // Get tuition fee relief
    @Query("SELECT tuitionFeeRelief FROM Tax WHERE userId = :userId")
    fun getTuitionFeeRelief(userId: String): Flow<Double>
}