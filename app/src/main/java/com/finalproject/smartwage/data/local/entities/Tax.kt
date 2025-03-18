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
    @get:PropertyName("totalPAYE") @set:PropertyName("totalPAYE") var totalPAYE: Double = 0.0,
    @get:PropertyName("totalUSC") @set:PropertyName("totalUSC") var totalUSC: Double = 0.0,
    @get:PropertyName("totalPRSI") @set:PropertyName("totalPRSI") var totalPRSI: Double = 0.0,
    @get:PropertyName("tuitionFeeRelief") @set:PropertyName("tuitionFeeRelief") var tuitionFeeRelief: Double = 0.0,
    @get:PropertyName("rentTaxCredit") @set:PropertyName("rentTaxCredit") var rentTaxCredit: Double = 0.0,
    @get:PropertyName("expectedPAYE") @set:PropertyName("expectedPAYE") var expectedPAYE: Double = 0.0,
    @get:PropertyName("expectedUSC") @set:PropertyName("expectedUSC") var expectedUSC: Double = 0.0,
    @get:PropertyName("expectedPRSI") @set:PropertyName("expectedPRSI") var expectedPRSI: Double = 0.0,
    @get:PropertyName("currentWeek") @set:PropertyName("currentWeek") var currentWeek: Int = 0,
    @get:PropertyName("taxOwed") @set:PropertyName("taxOwed") var taxOwed: Double = 0.0,
    @get:PropertyName("lastCalculated") @set:PropertyName("lastCalculated") var lastCalculated: Long = System.currentTimeMillis()
)