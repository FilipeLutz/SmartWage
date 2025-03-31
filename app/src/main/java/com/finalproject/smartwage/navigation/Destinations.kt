package com.finalproject.smartwage.navigation

/**
 * Destinations.kt
 *
 * This file contains the navigation destinations for the application.
 * It defines the routes for different screens in the app.
 */

sealed class Destinations(val route: String) {
    // Root destination
    object Login : Destinations("login")
    object SignUp : Destinations("signup")
    // Main destination
    data class Dashboard(val userId: String) : Destinations("dashboard/{userId}") {
        fun createRoute(userId: String) = "dashboard/$userId"
    }
    // Other destinations
    object Income : Destinations("income")
    object Expense : Destinations("expense")
    object TaxCredit : Destinations("taxcredit")
    object Profile : Destinations("profile")
    object Settings : Destinations("settings")
    object AboutApp : Destinations("AboutApp")
    object Tutorial : Destinations("tutorial")
}

