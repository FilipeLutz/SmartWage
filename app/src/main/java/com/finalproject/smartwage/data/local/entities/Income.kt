package com.finalproject.smartwage.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.finalproject.smartwage.data.model.Income

@Entity(tableName = "income")
data class Income(
    @PrimaryKey val id: String,
    val userId: String,
    val source: String,
    val amount: Double,
    val date: Long = System.currentTimeMillis()
)