package com.finalproject.smartwage.data.remote

import com.finalproject.smartwage.data.model.User
import com.finalproject.smartwage.data.model.Income
import com.finalproject.smartwage.data.model.Expense
import com.finalproject.smartwage.data.model.Tax
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    // Save User to Firestore
    suspend fun saveUser(user: User) {
        db.collection("users").document(user.id).set(user).await()
    }

    // User by ID
    suspend fun getUser(userId: String): User? {
        return db.collection("users").document(userId).get().await().toObject(User::class.java)
    }

    // Save Income
    suspend fun saveIncome(income: Income) {
        db.collection("income").document(income.id).set(income).await()
    }

    // User's Incomes
    suspend fun getUserIncomes(userId: String): List<Income> {
        return db.collection("income").whereEqualTo("userId", userId).get().await().toObjects(Income::class.java)
    }

    // Save Expense
    suspend fun saveExpense(expense: Expense) {
        db.collection("expenses").document(expense.id).set(expense).await()
    }

    // User's Expenses
    suspend fun getUserExpenses(userId: String): List<Expense> {
        return db.collection("expenses").whereEqualTo("userId", userId).get().await().toObjects(Expense::class.java)
    }

    // Save Tax
    suspend fun saveTax(tax: Tax) {
        db.collection("taxes").document(tax.id).set(tax).await()
    }

    // User's Tax Information
    suspend fun getUserTax(userId: String): List<Tax> {
        return db.collection("taxes").whereEqualTo("userId", userId).get().await().toObjects(Tax::class.java)
    }

    // Calculate Tax
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
            val higherTax = (totalIncome - lowerTaxThreshold) * higherTaxRate  // 40% on the rest
            lowerTax + higherTax
        }

        val finalTax = taxOwed - taxCredit
        return if (finalTax > 0) finalTax else 0.0
    }
}