package com.finalproject.smartwage.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName
import java.util.UUID

/**
 * This data class represents an Expense entity in the local and remote database.
 * It contains properties such as id, userId, category, amount, date, and description.
 * The id is generated using UUID to ensure uniqueness.
 *
 * @property id Unique identifier for the expense.
 * @property userId Identifier for the user associated with the expense.
 * @property category Category of the expense.
 * @property amount Amount spent on the expense.
 * @property date Date of the expense in milliseconds since epoch.
 * @property description Description of the expense.
 */

@Entity(tableName = "expenses")
// Data class for Expenses
data class Expenses(
    // Primary key
    @PrimaryKey @get:PropertyName("id") @set:PropertyName("id") var id: String = UUID.randomUUID().toString(),
    @get:PropertyName("userId") @set:PropertyName("userId") var userId: String = "",
    @get:PropertyName("category") @set:PropertyName("category") var category: String = "",
    @get:PropertyName("amount") @set:PropertyName("amount") var amount: Double = 0.0,
    @get:PropertyName("date") @set:PropertyName("date") var date: Long = System.currentTimeMillis(),
    @get:PropertyName("description") @set:PropertyName("description") var description: String = ""
)