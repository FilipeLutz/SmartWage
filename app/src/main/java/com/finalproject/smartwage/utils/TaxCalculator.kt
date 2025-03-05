package com.finalproject.smartwage.utils

// Tax Calculator for saved tax calculations
object TaxCalculator {

    private const val TAX_THRESHOLD = 44000.0  // Annual PAYE threshold
    private const val TAX_RATE_LOW = 0.20 // PAYE rate for income below threshold
    private const val TAX_RATE_HIGH = 0.40 // PAYE rate for income above threshold
    private const val TAX_CREDIT = 4000.0 // Tax credit

    // USC Brackets
    private val USC_BRACKETS = listOf(
        12012.0 to 0.005,  // 0.5% on first €12,012
        15370.0 to 0.02,   // 2% on next €15,370
        42662.0 to 0.03,   // 3% on next €42,662
        Double.MAX_VALUE to 0.08  // 8% on anything above
    )

    // PRSI Thresholds
    enum class PaymentFrequency(val divisor: Double) {
        WEEKLY(52.0), FORTNIGHTLY(26.0), MONTHLY(12.0)
    }

    /**
     * Calculates tax based on ANNUAL income.
     */
    fun calculateTax(income: Double, frequency: PaymentFrequency): Triple<Double, Double, Double> {
        val annualIncome = income * frequency.divisor  // Convert to annual income
        val calculatedPAYE = calculatePAYE(annualIncome)
        val calculatedUSC = calculateUSC(annualIncome)
        val calculatedPRSI = calculatePRSI(annualIncome)
        return Triple(calculatedPAYE / frequency.divisor, calculatedUSC / frequency.divisor, calculatedPRSI / frequency.divisor)
    }

    /**
     * PAYE Calculation
     */
    private fun calculatePAYE(annualIncome: Double): Double {
        var payeTax = if (annualIncome <= TAX_THRESHOLD) {
            annualIncome * TAX_RATE_LOW
        } else {
            (TAX_THRESHOLD * TAX_RATE_LOW) + ((annualIncome - TAX_THRESHOLD) * TAX_RATE_HIGH)
        }
        payeTax -= TAX_CREDIT  // Apply tax credit
        return maxOf(payeTax, 0.0) // No negative tax
    }

    /**
     * USC Calculation
     */
    private fun calculateUSC(annualIncome: Double): Double {
        if (annualIncome <= 13000) return 0.0  // No USC if income below 13,000

        var calculatedUSC = 0.0
        var remainingIncome = annualIncome

        for ((bracket, rate) in USC_BRACKETS) {
            if (remainingIncome > 0) {
                val taxable = minOf(remainingIncome, bracket)
                calculatedUSC += taxable * rate
                remainingIncome -= taxable
            } else break
        }
        return calculatedUSC
    }

    /**
     * PRSI Calculation based on ANNUAL income.
     */
    private fun calculatePRSI(annualIncome: Double): Double {
        val weeklyIncome = annualIncome / 52.0 // Convert annual to weekly
        val weeklyThreshold = 352.0
        val weeklyTaperedLimit = 424.0
        val prsiRate = 0.041

        if (weeklyIncome <= weeklyThreshold) return 0.0

        var prsiCharge = weeklyIncome * prsiRate
        if (weeklyIncome in (weeklyThreshold + 0.01)..weeklyTaperedLimit) {
            val excess = weeklyIncome - weeklyThreshold
            val prsiCredit = maxOf(12.0 - (excess / 6.0), 0.0)
            prsiCharge -= prsiCredit
        }
        return maxOf(prsiCharge, 0.0) * 52 // Convert back to annual PRSI
    }
}

// Quick Tax Calculator
object QuickTaxCalculator {

    private const val TAX_THRESHOLD = 44000.0
    private const val TAX_RATE_LOW = 0.20
    private const val TAX_RATE_HIGH = 0.40
    private const val TAX_CREDIT = 4000.0

    // USC Brackets
    private val USC_BRACKETS = listOf(
        12012.0 to 0.005,
        15370.0 to 0.02,
        42662.0 to 0.03,
        Double.MAX_VALUE to 0.08
    )

    /**
     * Calculates PAYE, USC, and PRSI for **Quick Tax Calculation**.
     */
    fun calculateQuickTax(annualIncome: Double): Triple<Double, Double, Double> {
        val paye = calculatePAYE(annualIncome)
        val usc = calculateUSC(annualIncome)
        val prsi = calculatePRSI(annualIncome)

        return Triple(paye, usc, prsi)
    }

    /**
     * PAYE Calculation
     */
    private fun calculatePAYE(income: Double): Double {
        var payeTax = if (income <= TAX_THRESHOLD) {
            income * TAX_RATE_LOW
        } else {
            (TAX_THRESHOLD * TAX_RATE_LOW) + ((income - TAX_THRESHOLD) * TAX_RATE_HIGH)
        }

        payeTax -= TAX_CREDIT
        return maxOf(payeTax, 0.0)
    }

    /**
     * USC Calculation
     */
    private fun calculateUSC(income: Double): Double {
        if (income <= 13000) return 0.0

        var calculatedUSC = 0.0
        var remainingIncome = income

        for ((bracket, rate) in USC_BRACKETS) {
            if (remainingIncome > 0) {
                val taxable = minOf(remainingIncome, bracket)
                calculatedUSC += taxable * rate
                remainingIncome -= taxable
            } else break
        }

        return calculatedUSC
    }

    /**
     * PRSI Calculation
     */
    private fun calculatePRSI(income: Double): Double {
        return if (income > 18304) income * 0.041 else 0.0
    }
}