package com.finalproject.smartwage.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName
import java.util.*

@Entity(tableName = "income")
data class Income(
    @PrimaryKey @get:PropertyName("id") @set:PropertyName("id") var id: String = UUID.randomUUID().toString(),
    @get:PropertyName("userId") @set:PropertyName("userId") var userId: String = "",
    @get:PropertyName("source") @set:PropertyName("source") var source: String = "",
    @get:PropertyName("amount") @set:PropertyName("amount") var amount: Double = 0.0,
    @get:PropertyName("paye") @set:PropertyName("paye") var paye: Double = 0.0,
    @get:PropertyName("usc") @set:PropertyName("usc") var usc: Double = 0.0,
    @get:PropertyName("prsi") @set:PropertyName("prsi") var prsi: Double = 0.0,
    @get:PropertyName("date") @set:PropertyName("date") var date: Long = System.currentTimeMillis(),
    @get:PropertyName("frequency") @set:PropertyName("frequency") var frequency: String = "",
    @get:PropertyName("payPeriod") @set:PropertyName("payPeriod") var payPeriod: Int = 0,
    @get:PropertyName("totalIncome") @set:PropertyName("totalIncome") var totalIncome: Double = 0.0,
    @get:PropertyName("overpaidTax") @set:PropertyName("overpaidTax") var overpaidTax: Double = 0.0,
    @get:PropertyName("underpaidTax") @set:PropertyName("underpaidTax") var underpaidTax: Double = 0.0
)