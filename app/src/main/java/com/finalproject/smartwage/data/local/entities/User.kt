package com.finalproject.smartwage.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName

/**
 * User data class representing a user in the application.
 * It contains various properties related to the user,
 *
 * @property id The unique identifier for the user.
 * @property name The name of the user.
 * @property email The email address of the user.
 * @property phoneNumber The phone number of the user.
 * @property taxCredit The tax credit amount for the user.
 * @property profilePicture The URL of the user's profile picture.
 * @property taxPaid The total tax paid by the user.
 * @property overpaidTax The total overpaid tax by the user.
 * @property underpaidTax The total underpaid tax by the user.
 * @property totalIncome The total income of the user.
 * @property totalExpenses The total expenses of the user.
 */

@Entity(tableName = "users")
// Data class for User
data class User(
    // Primary key
    @PrimaryKey @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
    @get:PropertyName("email") @set:PropertyName("email") var email: String = "",
    @get:PropertyName("phoneNumber") @set:PropertyName("phoneNumber") var phoneNumber: String = "",
    @get:PropertyName("taxCredit") @set:PropertyName("taxCredit") var taxCredit: Double = 4000.0,
    @get:PropertyName("profilePicture") @set:PropertyName("profilePicture") var profilePicture: String? = null,
    @get:PropertyName("taxPaid") @set:PropertyName("taxPaid") var taxPaid: Double = 0.0,
    @get:PropertyName("overpaidTax") @set:PropertyName("overpaidTax") var overpaidTax: Double = 0.0,
    @get:PropertyName("underpaidTax") @set:PropertyName("underpaidTax") var underpaidTax: Double = 0.0,
    @get:PropertyName("totalIncome") @set:PropertyName("totalIncome") var totalIncome: Double = 0.0,
    @get:PropertyName("totalExpenses") @set:PropertyName("totalExpenses") var totalExpenses: Double = 0.0,
)