package com.finalproject.smartwage.data.remote

import android.annotation.SuppressLint
import com.finalproject.smartwage.data.local.entities.Expense
import com.finalproject.smartwage.data.local.entities.Income
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

    // Save User to Firestore
    suspend fun saveUser(user: User) {
        try {
            db.collection("users").document(user.id).set(user).await()
            Timber.d("FirestoreService: User saved successfully: ${user.id}")
        } catch (e: Exception) {
            Timber.e(e, "FirestoreService: Error saving user")
        }
    }

    // Update User Profile
    suspend fun updateUser(user: User) {
        try {
            db.collection("users").document(user.id).set(user, SetOptions.merge()).await()
            Timber.d("FirestoreService: User updated successfully: ${user.id}")
        } catch (e: Exception) {
            Timber.e(e, "FirestoreService: Error updating user")
        }
    }

    // Get User from Firestore
    suspend fun getUser(userId: String): User? {
        return try {
            val snapshot = db.collection("users").document(userId).get().await()
            val user = snapshot.toObject(User::class.java)
            Timber.d("FirestoreService", "Fetched user: ${user?.id}")
            user
        } catch (e: Exception) {
            Timber.e(e, "FirestoreService", "Error fetching user")
            null
        }
    }

    // Delete User from Firestore
    suspend fun deleteUser(userId: String) {
        try {
            db.collection("users").document(userId).delete().await()
            Timber.d("FirestoreService", "User deleted: $userId")
        } catch (e: Exception) {
            Timber.e(e, "FirestoreService", "Error deleting user")
        }
    }

    // Generate Firestore Income ID
    fun generateIncomeId(): String {
        return db.collection("incomes").document().id
    }

    // Save Income
    suspend fun saveIncome(income: Income) {
        try {
            db.collection("incomes").document(income.id).set(income).await()
            Timber.d("FirestoreService", "Income saved successfully: ${income.id}")
        } catch (e: Exception) {
            Timber.e(e, "FirestoreService", "Error saving income")
        }
    }

    // Get User Incomes from Firestore
    suspend fun getUserIncomes(userId: String): List<Income> {
        return try {
            val snapshot = db.collection("incomes")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val incomes = snapshot.toObjects(Income::class.java)
            Timber.d("FirestoreService", "Fetched ${incomes.size} incomes for user $userId")
            incomes
        } catch (e: Exception) {
            Timber.e(e, "FirestoreService", "Error fetching incomes")
            emptyList()  // Return empty list if error
        }
    }

    // Delete Income from Firestore
    suspend fun deleteIncome(incomeId: String) {
        try {
            db.collection("incomes").document(incomeId).delete().await()
            Timber.d("FirestoreService", "Income deleted: $incomeId")
        } catch (e: Exception) {
            Timber.e(e, "FirestoreService", "Error deleting income")
        }
    }

    // Generate Firestore Expense ID
    fun generateExpenseId(): String {
        return FirebaseFirestore.getInstance().collection("expenses").document().id
    }

    // Save Expense
    suspend fun saveExpense(expense: Expense) {
        try {
            db.collection("expenses").document(expense.id).set(expense).await()
        } catch (e: Exception) {
            // Handle error (e.g., log or show a message)
            println("Error saving expense to Firestore: ${e.message}")
        }
    }

    // Get User Expenses from Firestore
    suspend fun getUserExpenses(userId: String): List<Expense> {
        return try {
            db.collection("expenses")
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .toObjects(Expense::class.java)
        } catch (e: Exception) {
            // Handle error (e.g., log or show a message)
            println("Error fetching expenses from Firestore: ${e.message}")
            emptyList()
        }
    }

    // Delete Expense from Firestore
    suspend fun deleteExpense(expenseId: String) {
        try {
            db.collection("expenses").document(expenseId).delete().await()
        } catch (e: Exception) {
            // Handle error (e.g., log or show a message)
            println("Error deleting expense from Firestore: ${e.message}")
        }
    }

    // Save Tax Record
    suspend fun saveTax(tax: Tax) {
        try {
            db.collection("taxes").document(tax.id).set(tax).await()
            Timber.d("FirestoreService", "Tax saved successfully: ${tax.id}")
        } catch (e: Exception) {
            Timber.e(e, "FirestoreService", "Error saving tax")
        }
    }

    // Get User's Tax Information
    suspend fun getUserTax(userId: String): List<Tax> {
        return try {
            val snapshot = db.collection("taxes")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val taxes = snapshot.toObjects(Tax::class.java)
            Timber.d("FirestoreService", "Fetched ${taxes.size} taxes for user $userId")
            taxes
        } catch (e: Exception) {
            Timber.e(e, "FirestoreService", "Error fetching taxes")
            emptyList()  // Return empty list if error
        }
    }

    // Delete User Data (if account is removed)
    suspend fun deleteUserData(userId: String) {
        db.collection("users").document(userId).delete().await()
        db.collection("incomes").whereEqualTo("userId", userId).get().await().documents.forEach {
            it.reference.delete().await()
        }
        db.collection("expenses").whereEqualTo("userId", userId).get().await().documents.forEach {
            it.reference.delete().await()
        }
        db.collection("taxes").whereEqualTo("userId", userId).get().await().documents.forEach {
            it.reference.delete().await()
        }
    }

    // Calculate Tax Logic
    suspend fun calculateTax(userId: String): Double {
        val incomes = getUserIncomes(userId)
        val totalIncome = incomes.sumOf { it.amount }
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
    }
}