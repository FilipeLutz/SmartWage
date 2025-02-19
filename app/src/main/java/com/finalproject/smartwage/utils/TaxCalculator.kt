package com.finalproject.smartwage.utils

object TaxCalculator {

    private const val TAX_THRESHOLD = 44000.0
    private const val TAX_RATE_LOW = 0.20 // 20%
    private const val TAX_RATE_HIGH = 0.40 // 40%
    private const val TAX_CREDIT = 4000.0

    fun calculateTax(incomes: Double): Double {
        return try {
            val totalIncome = incomes
            var calculatedTax = 0.0

            if (totalIncome <= TAX_THRESHOLD) {
                calculatedTax = totalIncome * TAX_RATE_LOW
            } else {
                calculatedTax = TAX_THRESHOLD * TAX_RATE_LOW
                calculatedTax += (totalIncome - TAX_THRESHOLD) * TAX_RATE_HIGH
            }

            calculatedTax -= TAX_CREDIT

            // Ensure the tax cannot be negative
            if (calculatedTax < 0) 0.0 else calculatedTax
        } catch (e: Exception) {
            println("Error calculating tax: ${e.message}")
            0.0
        }
    }
}
