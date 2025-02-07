package com.finalproject.smartwage.utils

import com.finalproject.smartwage.data.model.Income

object TaxCalculator {

    fun calculateTax(incomes: List<Income>): Double {
        val totalIncome = incomes.sumOf { it.amount }
        val taxableAboveThreshold = if (totalIncome > Constants.TAX_THRESHOLD) totalIncome - Constants.TAX_THRESHOLD else 0.0
        val taxLow = Constants.TAX_THRESHOLD * Constants.TAX_RATE_LOW
        val taxHigh = taxableAboveThreshold * Constants.TAX_RATE_HIGH
        return (taxLow + taxHigh) - Constants.TAX_CREDIT
    }
}