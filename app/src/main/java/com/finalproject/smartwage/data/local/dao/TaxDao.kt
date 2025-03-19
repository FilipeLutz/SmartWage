package com.finalproject.smartwage.data.local.dao

import androidx.room.*
import com.finalproject.smartwage.data.local.entities.Tax
import kotlinx.coroutines.flow.Flow

@Dao
interface TaxDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTax(tax: Tax)

    @Query("SELECT * FROM tax WHERE userId = :userId")
    fun getUserTax(userId: String): Flow<List<Tax>>

    @Query("DELETE FROM tax WHERE userId = :userId")
    suspend fun deleteTax(userId: String)

    @Query("SELECT rentTaxCredit FROM Tax WHERE userId = :userId")
    fun getRentTaxCredit(userId: String): Flow<Double>

    @Query("SELECT tuitionFeeRelief FROM Tax WHERE userId = :userId")
    fun getTuitionFeeRelief(userId: String): Flow<Double>
}