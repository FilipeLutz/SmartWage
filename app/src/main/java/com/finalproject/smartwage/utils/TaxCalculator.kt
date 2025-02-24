package com.finalproject.smartwage.utils

object TaxCalculator {

    private const val TAX_THRESHOLD = 44000.0
    private const val TAX_RATE_LOW = 0.20
    private const val TAX_RATE_HIGH = 0.40
    private const val TAX_CREDIT = 4000.0

    // USC Tax Brackets
    private val USC_BRACKETS = listOf(
        12012.0 to 0.005,  // 0.5% for first €12,012
        15370.0 to 0.02,   // 2% for next €15,370
        42662.0 to 0.03,   // 3% for next €42,662
        Double.MAX_VALUE to 0.08 // 8% on balance
    )

    fun calculateTax(income: Double, prsiRate: Double = 0.04): Triple<Double, Double, Double> {
        var calculatedPAYE = 0.0
        var calculatedUSC = 0.0
        var remainingIncome = income

        // PAYE Tax
        if (income <= TAX_THRESHOLD) {
            calculatedPAYE = income * TAX_RATE_LOW
        } else {
            calculatedPAYE = TAX_THRESHOLD * TAX_RATE_LOW
            calculatedPAYE += (income - TAX_THRESHOLD) * TAX_RATE_HIGH
        }
        calculatedPAYE -= TAX_CREDIT
        calculatedPAYE = maxOf(calculatedPAYE, 0.0)

        // USC Tax Calculation
        for ((bracket, rate) in USC_BRACKETS) {
            if (remainingIncome > 0) {
                val taxable = minOf(remainingIncome, bracket)
                calculatedUSC += taxable * rate
                remainingIncome -= taxable
            } else break
        }

        // PRSI Tax (Flat 4% unless income is below threshold)
        val calculatedPRSI = if (income > 18304) income * prsiRate else 0.0

        return Triple(calculatedPAYE, calculatedUSC, calculatedPRSI)
    }
}