package com.finalproject.smartwage.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tax")
data class Tax(
    @PrimaryKey val id: String,
    val userId: String,
    val income: Double,
    val taxPaid: Double,
    val taxYear: Int
)