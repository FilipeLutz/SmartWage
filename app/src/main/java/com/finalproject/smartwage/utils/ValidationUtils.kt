package com.finalproject.smartwage.utils

import android.util.Patterns

/**
 * Utility functions for validating user input.
 * This includes email validation and password validation.
 *
 * Email validation checks if the input string is a valid email format.
 * Password validation checks for:
 * - Minimum length (at least 8 characters)
 * - At least one uppercase letter
 * - At least one digit
 * - At least one special character
 *
 * Returns a list of errors if the password does not meet the criteria.
 */

// Email validation
fun isValidEmail(email: String): Boolean {
    // Check if the email is empty
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

// Password validation rules
enum class PasswordValidationError {
    // Constants for password validation errors
    TOO_SHORT,
    NO_UPPERCASE,
    NO_DIGIT,
    NO_SPECIAL_CHARACTER
}

// Validate password and return a list of errors
fun validatePassword(password: String): List<PasswordValidationError> {
    // List to hold validation errors
    val errors = mutableListOf<PasswordValidationError>()

    // Check minimum length
    if (password.length < 8) {
        errors.add(PasswordValidationError.TOO_SHORT)
    }

    // Check for at least one uppercase letter
    if (!password.contains(Regex("[A-Z]"))) {
        errors.add(PasswordValidationError.NO_UPPERCASE)
    }

    // Check for at least one digit
    if (!password.contains(Regex("[0-9]"))) {
        errors.add(PasswordValidationError.NO_DIGIT)
    }

    // Check for at least one special character
    if (!password.contains(Regex("[^A-Za-z0-9]"))) {
        errors.add(PasswordValidationError.NO_SPECIAL_CHARACTER)
    }
    // Return the list of errors
    return errors
}