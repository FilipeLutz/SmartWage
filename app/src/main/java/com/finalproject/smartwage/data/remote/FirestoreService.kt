package com.finalproject.smartwage.data.remote

import android.annotation.SuppressLint
import com.finalproject.smartwage.data.local.entities.Expenses
import com.finalproject.smartwage.data.local.entities.Income
import com.finalproject.smartwage.data.local.entities.Settings
import com.finalproject.smartwage.data.local.entities.Tax
import com.finalproject.smartwage.data.local.entities.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("TimberArgCount")
class FirestoreService @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

    // FirestoreService: saveUser function
    suspend fun saveUser(user: User) {
        try {
            db.collection("users").document(user.id).set(user).await()
            Timber.d("FirestoreService: User saved successfully: ${user.id}")
        } catch (e: Exception) {
            Timber.e(e, "Error saving user to Firestore")
        }
    }

    suspend fun getUser(userId: String): User? {
        return try {
            // Fetch the user from Firestore
            val snapshot = db.collection("users").document(userId).get().await()

            // If the document exists, convert it to User object
            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            // Log the error to debug Firestore-related issues
            Timber.e(e, "Error fetching user from Firestore: $userId")

            null
        }
    }

    // Delete User from Firestore
    suspend fun deleteUser(userId: String) {
        try {
            db.collection("users").document(userId).delete().await()
            Timber.d("FirestoreService: User deleted successfully: $userId")
        } catch (e: Exception) {
            Timber.e(e, "Error deleting user from Firestore")
        }
    }

    // Save User Settings to Firestore
    suspend fun saveUserSettings(userId: String, settings: Settings) {
        try {
            db.collection("users").document(userId).collection("settings")
                .document("user_settings").set(settings).await()
            Timber.d("FirestoreService: User settings saved successfully for $userId")
        } catch (e: Exception) {
            Timber.e(e, "Error saving user settings to Firestore")
        }
    }

    // Get User Settings from Firestore
    suspend fun getUserSettings(userId: String): Settings? {
        return try {
            val snapshot = db.collection("users").document(userId).collection("settings")
                .document("user_settings").get().await()

            snapshot.toObject(Settings::class.java)
        } catch (e: Exception) {
            Timber.e(e, "Error fetching user settings from Firestore: $userId")
            null
        }
    }

    // Update User Settings in Firestore
    suspend fun updateUserSettings(userId: String, settings: Settings) {
        try {
            db.collection("users").document(userId).collection("settings")
                .document("user_settings").set(settings, SetOptions.merge()).await()
            Timber.d("FirestoreService: User settings updated successfully for $userId")
        } catch (e: Exception) {
            Timber.e(e, "Error updating user settings in Firestore")
        }
    }

    // Delete User Settings from Firestore
    suspend fun deleteUserSettings(userId: String) {
        try {
            db.collection("users").document(userId).collection("settings")
                .document("user_settings").delete().await()
            Timber.d("FirestoreService: User settings deleted successfully for $userId")
        } catch (e: Exception) {
            Timber.e(e, "Error deleting user settings from Firestore")
        }
    }

    // Get incomes for a specific user from Firestore
    suspend fun getUserIncomes(userId: String): List<Income> {
        return try {
            db.collection("incomes")
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .toObjects(Income::class.java)
        } catch (e: Exception) {
            Timber.e(e, "Error fetching incomes from Firestore")
            emptyList()
        }
    }

    // Save Income to Firestore
    suspend fun saveIncome(income: Income) {
        try {
            db.collection("incomes").document(income.id).set(income).await()
        } catch (e: Exception) {
            Timber.e(e, "Error saving income to Firestore")
        }
    }

    // Update Income in Firestore
    suspend fun updateIncome(income: Income) {
        try {
            db.collection("incomes").document(income.id).set(income, SetOptions.merge()).await()
        } catch (e: Exception) {
            Timber.e(e, "Error updating income in Firestore")
        }
    }

    // Delete Income from Firestore
    suspend fun deleteIncome(incomeId: String, userId: String) {
        try {
            db.collection("incomes")
                .whereEqualTo("id", incomeId)
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .documents
                .forEach { it.reference.delete() }
        } catch (e: Exception) {
            Timber.e(e, "Error deleting income from Firestore")
        }
    }

    // Save Expense to Firestore
    suspend fun saveExpenses(expenses: Expenses) {
        try {
            db.collection("expenses").document(expenses.id).set(expenses).await()
            Timber.d("Expense saved successfully: $expenses")
        } catch (e: Exception) {
            Timber.e(e, "Error saving expense to Firestore")
        }
    }

    // Rent Tax Credit
    suspend fun getRentTaxCredit(userId: String): Double {
        return try {
            val taxRecord = db.collection("taxes")
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .toObjects(Tax::class.java)

            if (taxRecord.isNotEmpty()) {
                val rentTaxCredit = taxRecord.first().rentTaxCredit
                Timber.d("Fetched Rent Tax Credit: $rentTaxCredit")
                rentTaxCredit
            } else {
                0.0
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching rent tax credit")
            0.0
        }
    }

    // Tuition Fee Relief
    suspend fun getTuitionFeeRelief(userId: String): Double {
        return try {
            val taxRecord = db.collection("taxes")
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .toObjects(Tax::class.java)

            if (taxRecord.isNotEmpty()) {
                val tuitionRelief = taxRecord.first().tuitionFeeRelief
                Timber.d("Fetched Tuition Fee Relief: $tuitionRelief")
                tuitionRelief
            } else {
                0.0
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching tuition fee relief")
            0.0
        }
    }

    // Get Expenses for a Specific User
    suspend fun getUserExpenses(userId: String): List<Expenses> {
        return try {
            val expenses = db.collection("expenses")
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .toObjects(Expenses::class.java)

            Timber.d("Fetched expenses from Firestore: $expenses") // Log results
            expenses
        } catch (e: Exception) {
            Timber.e(e, "Error fetching expenses from Firestore")
            emptyList()
        }
    }

    // Update Expense in Firestore
    suspend fun updateExpenses(expenses: Expenses) {
        try {
            db.collection("expenses").document(expenses.id).set(expenses, SetOptions.merge()).await()
        } catch (e: Exception) {
            Timber.e(e, "Error updating expense in Firestore")
        }
    }

    // Delete Expense from Firestore
    suspend fun deleteExpenses(expenseId: String, string: String) {
        try {
            db.collection("expenses").document(expenseId).delete().await()
        } catch (e: Exception) {
            Timber.e(e, "Error deleting expense from Firestore")
        }
    }

    // Save Tax Record to Firestore
    suspend fun saveTax(tax: Tax) {
        try {
            db.collection("taxes").document(tax.id).set(tax).await()
            Timber.d("FirestoreService: Tax saved successfully: ${tax.id}")
        } catch (e: Exception) {
            Timber.e(e, "Error saving tax record to Firestore")
        }
    }

    // Get User's Tax Information
    suspend fun getUserTax(userId: String): List<Tax> {
        return try {
            db.collection("taxes")
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .toObjects(Tax::class.java)
        } catch (e: Exception) {
            Timber.e(e, "Error fetching taxes from Firestore")
            emptyList()
        }
    }

    // Delete All User Data (when deleting an account)
    suspend fun deleteUserData(userId: String) {
        try {
            db.collection("users").document(userId).delete().await()

            db.collection("incomes")
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .documents.forEach { it.reference.delete() }

            db.collection("expenses")
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .documents.forEach { it.reference.delete() }

            db.collection("taxes")
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .documents.forEach { it.reference.delete() }

            Timber.d("FirestoreService: Deleted all data for user $userId")
        } catch (e: Exception) {
            Timber.e(e, "Error deleting user data from Firestore")
        }
    }

    // Calculate Tax Based on User's Income
    suspend fun calculateTax(userId: String): Double {
        try {
            val incomes = getUserIncomes(userId) // Get user's incomes
            val totalIncome = incomes.sumOf { it.amount } // Sum of all incomes

            val taxCredit = 4000.0
            val lowerTaxThreshold = 44000.0
            val lowerTaxRate = 0.20
            val higherTaxRate = 0.40

            val taxOwed = if (totalIncome <= lowerTaxThreshold) {
                totalIncome * lowerTaxRate  // 20% on income up to €44,000
            } else {
                val lowerTax = lowerTaxThreshold * lowerTaxRate  // 20% on €44,000
                val higherTax = (totalIncome - lowerTaxThreshold) * higherTaxRate  // 40% on remainder
                lowerTax + higherTax
            }

            val finalTax = taxOwed - taxCredit
            return if (finalTax > 0) finalTax else 0.0
        } catch (e: Exception) {
            Timber.e(e, "Error calculating tax for user $userId")
            return 0.0
        }
    }
}