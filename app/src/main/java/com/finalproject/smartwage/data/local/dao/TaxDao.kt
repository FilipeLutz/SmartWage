package com.finalproject.smartwage.data.local.dao

import androidx.room.*
import com.finalproject.smartwage.data.local.entities.Tax

@Dao
interface TaxDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTax(tax: Tax)

    @Query("SELECT * FROM tax WHERE userId = :userId")
    suspend fun getUserTax(userId: String): List<Tax>

    @Query("DELETE FROM tax WHERE id = :taxId")
    suspend fun deleteTax(taxId: String)
}