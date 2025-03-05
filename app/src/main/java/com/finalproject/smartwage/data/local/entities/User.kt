package com.finalproject.smartwage.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName

@Entity(tableName = "users")
data class User(
    @PrimaryKey @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
    @get:PropertyName("email") @set:PropertyName("email") var email: String = "",
    @get:PropertyName("phoneNumber") @set:PropertyName("phoneNumber") var phoneNumber: String = "",
    @get:PropertyName("taxCredit") @set:PropertyName("taxCredit") var taxCredit: Double = 4000.0,
    @get:PropertyName("profilePicture") @set:PropertyName("profilePicture") var profilePicture: String? = null,
    @get:PropertyName("taxPaid") @set:PropertyName("taxPaid") var taxPaid: Double = 0.0,
    @get:PropertyName("taxOwed") @set:PropertyName("taxOwed") var taxOwed: Double = 0.0,
    @get:PropertyName("taxBack") @set:PropertyName("taxBack") var taxBack: Double = 0.0,
    @get:PropertyName("totalIncome") @set:PropertyName("totalIncome") var totalIncome: Double = 0.0,
    @get:PropertyName("totalExpenses") @set:PropertyName("totalExpenses") var totalExpenses: Double = 0.0,
    )