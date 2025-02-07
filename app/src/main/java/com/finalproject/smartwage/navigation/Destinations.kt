package com.finalproject.smartwage.navigation

sealed class Destinations(val route: String) {
    object Login : Destinations("login")
    object SignUp : Destinations("signup")
    object Dashboard : Destinations("dashboard")
    object Income : Destinations("income")
    object Expense : Destinations("expense")
    object TaxCredit : Destinations("taxcredit")
    object Profile : Destinations("profile")
    object Settings : Destinations("settings")
    object Help : Destinations("help")
}