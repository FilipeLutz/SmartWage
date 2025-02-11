package com.finalproject.smartwage.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tax")
data class Tax(
    @PrimaryKey val id: String,
    val userId: String,
    val totalIncome: Double,
    val taxOwed: Double,
    val lastCalculated: Long
)