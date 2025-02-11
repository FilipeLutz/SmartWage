package com.finalproject.smartwage.data.remote

import android.util.Log
import com.finalproject.smartwage.data.model.User
import com.finalproject.smartwage.data.model.Income
import com.finalproject.smartwage.data.model.Expense
import com.finalproject.smartwage.data.model.Tax
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreService @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

    // Save User to Firestore
    suspend fun saveUser(user: com.finalproject.smartwage.data.local.entities.User) {
        db.collection("users").document(user.id).set(user).await()
    }

    // Get User from Firestore
    suspend fun getUser(userId: String): User? {
        return db.collection("users").document(userId).get().await().toObject(User::class.java)
    }

    // Update User Profile
    suspend fun updateUser(user: User) {
        try {
            db.collection("users").document(user.id).set(user).await()
        } catch (e: Exception) {
            println("Error updating user: ${e.message}")
        }
    }

    // Delete User from Firestore
    suspend fun deleteUser(userId: String) {
        try {
            db.collection("users").document(userId).delete().await()
        } catch (e: Exception) {
            println("Error deleting user: ${e.message}")
        }
    }

    // Generate a Unique Income ID
    fun generateIncomeId(): String {
        return db.collection("incomes").document().id
    }

    // Save Income (Firestore)
    suspend fun saveIncome(income: com.finalproject.smartwage.data.local.entities.Income) {
        try {
            db.collection("incomes").document(income.id).set(income).await()
            Log.d("FirestoreService", "Income saved successfully: ${income.id}")
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error saving income", e)
        }
    }

    // Get User's Incomes
    suspend fun getUserIncomes(userId: String): List<Income> {
        return try {
            val snapshot = db.collection("incomes")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val incomes = snapshot.toObjects(Income::class.java)
            Log.d("FirestoreService", "Fetched ${incomes.size} incomes for user $userId")
            incomes
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error fetching incomes", e)
            emptyList()  // Return empty list if error
        }
    }

    // Delete Income (Firestore)
    suspend fun deleteIncome(incomeId: String) {
        try {
            db.collection("incomes").document(incomeId).delete().await()
            Log.d("FirestoreService", "Income deleted: $incomeId")
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error deleting income", e)
        }
    }

    // Generate Firestore Expense ID
    fun generateExpenseId(): String {
        return FirebaseFirestore.getInstance().collection("expenses").document().id
    }

    // Save Expense
    suspend fun saveExpense(expense: com.finalproject.smartwage.data.local.entities.Expense) {
        db.collection("expenses").document(expense.id).set(expense).await()
    }

    // Get User Expenses from Firestore
    suspend fun getUserExpenses(userId: String): List<Expense> {
        return db.collection("expenses")
            .whereEqualTo("userId", userId)
            .get()
            .await()
            .toObjects(Expense::class.java)
    }

    // Delete Expense from Firestore
    suspend fun deleteExpense(expenseId: String) {
        db.collection("expenses").document(expenseId).delete().await()
    }

    // Save Tax Record
    suspend fun saveTax(tax: com.finalproject.smartwage.data.local.entities.Tax) {
        db.collection("taxes").document(tax.id).set(tax).await()
    }

    // Get User's Tax Information
    suspend fun getUserTax(userId: String): List<Tax> {
        return db.collection("taxes")
            .whereEqualTo("userId", userId)
            .get().await()
            .toObjects(Tax::class.java)
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