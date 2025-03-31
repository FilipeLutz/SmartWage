package com.finalproject.smartwage.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.finalproject.smartwage.data.local.entities.Tax
import kotlinx.coroutines.flow.Flow

/**
 * TaxDao is an interface that defines the data access methods for the Tax entity.
 * It provides methods to insert, retrieve, and delete tax records from the database.
 *
 * @Dao annotation indicates that this is a Data Access Object.
 */

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