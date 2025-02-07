package com.finalproject.smartwage.data.local.dao

import androidx.room.*
import com.finalproject.smartwage.data.local.entities.Tax
import kotlinx.coroutines.flow.Flow

@Dao
interface TaxDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaxRecord(tax: Tax)

    @Query("SELECT * FROM tax WHERE userId = :userId")
    suspend fun getUserTaxRecords(userId: String): List<Tax>

    @Query("DELETE FROM tax WHERE id = :taxId")
    suspend fun deleteTaxRecord(taxId: String)
}