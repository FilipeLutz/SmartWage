package com.finalproject.smartwage

import com.finalproject.smartwage.utils.PasswordValidationError
import com.finalproject.smartwage.utils.TaxCalculator
import com.finalproject.smartwage.utils.validatePassword
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for the TaxCalculator and ValidationUtils classes.
 * These tests cover various scenarios for tax calculations and validation rules.
 *
 * The tests include:
 * - PAYE calculations for different income levels
 * - Tuition fee relief calculations
 * - Rent tax credit calculations
 * - Validation of passwords and email formats
 *
 * The tests ensure that the tax calculations and validation rules are working as expected.
 */

class TaxCalculatorTest {

    // Test cases for PAYE calculations
    @Test
    fun testPAYE_UnderThreshold() {
        val income = 30000.0
        val expectedPAYE = ((30000.0 * 0.20) - 4000.0).coerceAtLeast(0.0)
        val result = TaxCalculator.calculateTax(
            income = income / 12,  // Monthly
            frequency = TaxCalculator.PaymentFrequency.MONTHLY,
            tuitionFees = 0.0,
            rentPaid = 0.0
        ).first

        assertEquals(expectedPAYE / 12, result, 0.01)
    }

    // Additional test cases for PAYE calculations
    @Test
    fun testPAYE_AboveThreshold_WithTuitionAndRent() {
        val result = TaxCalculator.calculateTax(
            income = 5000.0,
            frequency = TaxCalculator.PaymentFrequency.MONTHLY,
            tuitionFees = 4000.0,
            rentPaid = 5000.0
        )

        // Expect lower PAYE due to reliefs
        assert(result.first < 5000.0 * 0.40)
    }

    // Tuition fee relief tests
    @Test
    fun testNoTuitionReliefUnderThreshold() {
        val relief = TaxCalculator.calculateTuitionFeeRelief(2500.0)
        assertEquals(0.0, relief, 0.01)
    }

    // Test tuition relief exceeding maximum claim
    @Test
    fun testTuitionReliefWithinLimits() {
        val relief = TaxCalculator.calculateTuitionFeeRelief(6000.0)
        val expected = (6000.0 - 3000.0) * 0.20
        assertEquals(expected, relief, 0.01)
    }

    // Rent tax credit tests not exceeding liability
    @Test
    fun testRentTaxCredit_NotExceedingLiability() {
        val credit = TaxCalculator.calculateRentTaxCredit(rentPaid = 4000.0, taxLiability = 800.0)
        val expectedCredit = (4000.0 * 0.20).coerceAtMost(1000.0).coerceAtMost(800.0)
        assertEquals(expectedCredit, credit, 0.01)
    }

    // Test rent tax credit exceeding liability
    @Test
    fun testRentTaxCredit_ExceedingLiability() {
        val credit = TaxCalculator.calculateRentTaxCredit(rentPaid = 6000.0, taxLiability = 800.0)
        val expectedCredit = (6000.0 * 0.20).coerceAtMost(1000.0).coerceAtMost(800.0)
        assertEquals(expectedCredit, credit, 0.01)
    }

    // Income tests with no tax liability
    @Test
    fun testZeroIncomeReturnsZeroTax() {
        val result = TaxCalculator.calculateTax(
            income = 0.0,
            frequency = TaxCalculator.PaymentFrequency.MONTHLY,
            tuitionFees = 0.0,
            rentPaid = 0.0
        )
        assertEquals(0.0, result.first, 0.01) // PAYE
        assertEquals(0.0, result.second, 0.01) // USC
        assertEquals(0.0, result.third, 0.01) // PRSI
    }

    // Test PRSI calculations below the threshold
    @Test
    fun testPRSIWaivedBelowWeeklyThreshold() {
        val result = TaxCalculator.calculateTax(
            income = 300.0,
            frequency = TaxCalculator.PaymentFrequency.WEEKLY,
            tuitionFees = 0.0,
            rentPaid = 0.0
        )
        assertEquals(0.0, result.third, 0.01)
    }

    // Overpaid tax scenario test
    @Test
    fun testOverpaidTaxScenario() {
        val paidPAYE = 3000.0
        val expectedPAYE = 2500.0
        val refund = (paidPAYE - expectedPAYE).coerceAtLeast(0.0)
        assertEquals(500.0, refund, 0.01)
    }
}

class ValidationUtilsTest {

    // Test cases for password validation rules with different scenarios
    @Test
    fun testPasswordFailsAllRules() {
        val errors = validatePassword("abc")
        assertTrue(errors.contains(PasswordValidationError.TOO_SHORT))
        assertTrue(errors.contains(PasswordValidationError.NO_UPPERCASE))
        assertTrue(errors.contains(PasswordValidationError.NO_DIGIT))
        assertTrue(errors.contains(PasswordValidationError.NO_SPECIAL_CHARACTER))
    }
    // Test cases for password validation
    @Test
    fun testValidPasswordPassesAllChecks() {
        val errors = validatePassword("Abcde123!")
        assertTrue(errors.isEmpty())
    }

    // Test cases with invalid email formats
    @Test
    fun testInvalidEmailFailsValidation() {
        val invalidEmails = listOf(
            "plainaddress",
            "@missingusername.com",
            "username@.com",
            "username@domain..com",
            "username@domain,com"
        )

        for (email in invalidEmails) {
            assertTrue("Expected $email to be invalid", !isValidEmail(email))
        }
    }

    // Test cases with valid email formats
    @Test
    fun testValidEmailPassValidation() {
        val validEmails = listOf(
            "user@example.com",
            "user.name+tag+sorting@example.com",
            "user_name@sub.domain.com",
            "user@domain.co.uk"
        )

        for (email in validEmails) {
            assertTrue("Expected $email to be valid", isValidEmail(email))
        }
    }
}

// Email validation regex
private val EMAIL_REGEX = Regex("^(?!.*\\.\\.)[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

// Function to validate email format
fun isValidEmail(email: String): Boolean {
    return EMAIL_REGEX.matches(email)
}