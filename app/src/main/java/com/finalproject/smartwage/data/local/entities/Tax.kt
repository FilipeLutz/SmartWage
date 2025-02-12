package com.finalproject.smartwage.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName
import java.util.*

@Entity(tableName = "tax")
data class Tax(
    @PrimaryKey @get:PropertyName("id") @set:PropertyName("id") var id: String = UUID.randomUUID().toString(),
    @get:PropertyName("userId") @set:PropertyName("userId") var userId: String = "",
    @get:PropertyName("totalIncome") @set:PropertyName("totalIncome") var totalIncome: Double = 0.0,
    @get:PropertyName("taxOwed") @set:PropertyName("taxOwed") var taxOwed: Double = 0.0,
    @get:PropertyName("lastCalculated") @set:PropertyName("lastCalculated") var lastCalculated: Long = System.currentTimeMillis()
)