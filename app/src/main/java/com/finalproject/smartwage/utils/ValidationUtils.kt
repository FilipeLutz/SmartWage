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

// Error messages for password validation
fun getPasswordErrorMessage(
    errors: List<PasswordValidationError>): String
{
    return when {
        errors.isEmpty() -> "Password is valid."
        errors.size == 1 -> when (errors[0]) {
            PasswordValidationError.TOO_SHORT ->
                "Password must be at least 8 characters long."
            PasswordValidationError.NO_UPPERCASE ->
                "Password must contain at least one uppercase letter."
            PasswordValidationError.NO_DIGIT ->
                "Password must contain at least one digit."
            PasswordValidationError.NO_SPECIAL_CHARACTER ->
                "Password must contain at least one special character."
        }
        else -> "Password is invalid for the following reasons:\n" +
                errors.joinToString("\n") { error ->
                    when (error) {
                        PasswordValidationError.TOO_SHORT ->
                            "* Must be at least 8 characters long."
                        PasswordValidationError.NO_UPPERCASE ->
                            "* Must contain at least one uppercase letter."
                        PasswordValidationError.NO_DIGIT ->
                            "* Must contain at least one digit."
                        PasswordValidationError.NO_SPECIAL_CHARACTER ->
                            "* Must contain at least one special character."
                    }
                }
    }
}