package com.finalproject.smartwage.utils

// Tax Calculator for saved tax calculations
object TaxCalculator {

    private const val TAX_THRESHOLD = 44000.0  // Annual PAYE threshold
    private const val TAX_RATE_LOW = 0.20 // PAYE rate for income below threshold
    private const val TAX_RATE_HIGH = 0.40 // PAYE rate for income above threshold
    private const val TAX_CREDIT = 4000.0 // Standard Tax Credit

    private const val FULL_TIME_DISREGARD = 3000.0 // Tuition Fee disregard
    private const val MAX_TUITION_CLAIM = 7000.0 // Max tuition fee eligible for relief
    private const val TUITION_RELIEF_RATE = 0.20 // 20% tax relief

    private const val RENT_CREDIT_2024_2025 = 1000.0 // Rent tax credit for single person
    private const val RENT_TAX_RELIEF = 0.20 // 20% rent tax relief

    enum class PaymentFrequency(val divisor: Double) {
        WEEKLY(52.0), FORTNIGHTLY(26.0), MONTHLY(12.0)
    }

    /**
     * Calculates tax based on ANNUAL income.
     */
    fun calculateTax(
        income: Double,
        frequency: PaymentFrequency,
        tuitionFees: Double,
        rentPaid: Double
    ): Triple<Double, Double, Double> {
        val annualIncome = income * frequency.divisor
        val calculatedPAYE = calculatePAYE(annualIncome)
        val calculatedUSC = calculateUSC(annualIncome)
        val calculatedPRSI = calculatePRSI(annualIncome)

        val tuitionRelief = calculateTuitionFeeRelief(tuitionFees)
        val rentCredit = calculateRentTaxCredit(rentPaid, calculatedPAYE)

        val totalTaxReduction = tuitionRelief + rentCredit
        val finalPAYE = maxOf(calculatedPAYE - totalTaxReduction, 0.0)

        return Triple(finalPAYE / frequency.divisor, calculatedUSC / frequency.divisor, calculatedPRSI / frequency.divisor)
    }

    private fun calculatePAYE(annualIncome: Double): Double {
        var payeTax = if (annualIncome <= TAX_THRESHOLD) {
            annualIncome * TAX_RATE_LOW
        } else {
            (TAX_THRESHOLD * TAX_RATE_LOW) + ((annualIncome - TAX_THRESHOLD) * TAX_RATE_HIGH)
        }
        payeTax -= TAX_CREDIT
        return maxOf(payeTax, 0.0)
    }

    private fun calculateUSC(income: Double): Double {
        // No USC on income up to €13,000
        if (income <= 13000) return 0.0

        var calculatedUSC = 0.0
        var remainingIncome = income

        // Apply 0.5% on the first €12,012
        val firstBracket = minOf(remainingIncome, 12012.0)
        calculatedUSC += firstBracket * 0.005
        remainingIncome -= firstBracket

        // Apply 2% on the next €3,358
        val secondBracket = minOf(remainingIncome, 3358.0)
        calculatedUSC += secondBracket * 0.02
        remainingIncome -= secondBracket

        // Apply 3% on the next €16,630
        val thirdBracket = minOf(remainingIncome, 16630.0)
        calculatedUSC += thirdBracket * 0.03
        remainingIncome -= thirdBracket

        // Apply 8% on the rest (if any)
        if (remainingIncome > 0) {
            calculatedUSC += remainingIncome * 0.08
        }

        return calculatedUSC
    }

    private fun calculatePRSI(annualIncome: Double): Double {
        val weeklyIncome = annualIncome / 52.0
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
        return maxOf(prsiCharge, 0.0) * 52
    }

    internal fun calculateTuitionFeeRelief(tuitionFees: Double): Double {
        if (tuitionFees <= FULL_TIME_DISREGARD) return 0.0
        val claimableAmount = minOf(tuitionFees, MAX_TUITION_CLAIM) - FULL_TIME_DISREGARD
        return maxOf(0.0, claimableAmount * TUITION_RELIEF_RATE)
    }

    internal fun calculateRentTaxCredit(rentPaid: Double, taxLiability: Double): Double {
        val rentRelief = minOf(rentPaid * RENT_TAX_RELIEF, RENT_CREDIT_2024_2025)
        return minOf(rentRelief, taxLiability)
    }
}

// Quick Tax Calculator
object QuickTaxCalculator {

    private const val TAX_THRESHOLD = 44000.0
    private const val TAX_RATE_LOW = 0.20
    private const val TAX_RATE_HIGH = 0.40
    private const val TAX_CREDIT = 4000.0

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
        // Calculate PAYE tax before applying the credit
        var payeTax = if (income <= TAX_THRESHOLD) {
            income * TAX_RATE_LOW
        } else {
            (TAX_THRESHOLD * TAX_RATE_LOW) + ((income - TAX_THRESHOLD) * TAX_RATE_HIGH)
        }

        // Apply the tax credit only to the PAYE
        payeTax -= TAX_CREDIT

        // Ensure PAYE doesn't go below 0
        return maxOf(payeTax, 0.0)
    }

    /**
     * USC Calculation
     */
    private fun calculateUSC(income: Double): Double {
        // No USC on income up to €13,000
        if (income <= 13000) return 0.0

        var calculatedUSC = 0.0
        var remainingIncome = income

        // Apply 0.5% on the first €12,012
        val firstBracket = minOf(remainingIncome, 12012.0)
        calculatedUSC += firstBracket * 0.005
        remainingIncome -= firstBracket

        // Apply 2% on the next €3,358
        val secondBracket = minOf(remainingIncome, 3358.0)
        calculatedUSC += secondBracket * 0.02
        remainingIncome -= secondBracket

        // Apply 3% on the next €16,630
        val thirdBracket = minOf(remainingIncome, 16630.0)
        calculatedUSC += thirdBracket * 0.03
        remainingIncome -= thirdBracket

        // Apply 8% on the rest of the income
        if (remainingIncome > 0) {
            calculatedUSC += remainingIncome * 0.08
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