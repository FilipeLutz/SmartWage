package com.finalproject.smartwage.navigation

sealed class Destinations(val route: String) {
    object Login : Destinations("login")
    object SignUp : Destinations("signup")

    data class Dashboard(val userId: String) : Destinations("dashboard/{userId}") {
        fun createRoute(userId: String) = "dashboard/$userId"
    }

    object Income : Destinations("income")
    object Expense : Destinations("expense")
    object TaxCredit : Destinations("taxcredit")
    object Profile : Destinations("profile")
    object Settings : Destinations("settings")
    object Help : Destinations("help")
}

