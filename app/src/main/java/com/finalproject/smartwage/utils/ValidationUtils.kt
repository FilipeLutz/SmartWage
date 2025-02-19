package com.finalproject.smartwage.utils

import android.util.Patterns

// Email validation
fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

// Password validation rules
enum class PasswordValidationError {
    TOO_SHORT,
    NO_UPPERCASE,
    NO_DIGIT,
    NO_SPECIAL_CHARACTER
}

// Validate password and return a list of errors
fun validatePassword(password: String): List<PasswordValidationError> {
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

    return errors
}