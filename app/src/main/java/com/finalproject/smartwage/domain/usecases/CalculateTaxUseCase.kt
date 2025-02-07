package com.finalproject.smartwage.domain.usecases

import com.finalproject.smartwage.data.model.Income

class CalculateTaxUseCase {

    fun execute(incomes: List<Income>): Double {
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