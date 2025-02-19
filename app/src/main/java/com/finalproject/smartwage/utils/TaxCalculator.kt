package com.finalproject.smartwage.utils

object TaxCalculator {

    private const val TAX_THRESHOLD = 44000.0
    private const val TAX_RATE_LOW = 0.20
    private const val TAX_RATE_HIGH = 0.40
    private const val TAX_CREDIT = 4000.0

    fun calculateTax(incomes: Double): Double {
        return try {
            val totalIncome = incomes
            val taxableAboveThreshold = if (totalIncome > TAX_THRESHOLD) totalIncome - TAX_THRESHOLD else 0.0
            val taxLow = TAX_THRESHOLD * TAX_RATE_LOW
            val taxHigh = taxableAboveThreshold * TAX_RATE_HIGH
            (taxLow + taxHigh) - TAX_CREDIT
        } catch (e: Exception) {
            println("Error calculating tax: ${e.message}")
            0.0
        }
    }
}